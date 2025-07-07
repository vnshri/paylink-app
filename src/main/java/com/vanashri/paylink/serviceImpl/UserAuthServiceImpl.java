package com.vanashri.paylink.serviceImpl;

import com.vanashri.paylink.dao.UsersAuthRepository;
import com.vanashri.paylink.dto.LoginResponseDto;
import com.vanashri.paylink.dto.UserRegisterRequestDto;
import com.vanashri.paylink.entity.UserEntity;
import com.vanashri.paylink.enums.Role;
import com.vanashri.paylink.exception.UserEmailAlreadyExistsException;
import com.vanashri.paylink.mapper.UserRegisterMapper;
import com.vanashri.paylink.service.UserAuthService;
import com.vanashri.paylink.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UsersAuthRepository usersAuthRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register New User
     */
    @Override
    public void addUser(UserRegisterRequestDto userRegisterRequestDto) {
        log.info("Saving user information to database");

        if (usersAuthRepository.existsByEmail(userRegisterRequestDto.getEmail())) {
            throw new UserEmailAlreadyExistsException("User already exists with email: " + userRegisterRequestDto.getEmail());
        }

        // Forcefully set the role to USER
        userRegisterRequestDto.setRole(Role.USER);

        // Encode password
        userRegisterRequestDto.setPassword(passwordEncoder.encode(userRegisterRequestDto.getPassword()));

        UserEntity user = UserRegisterMapper.mapToEntity(userRegisterRequestDto);
        usersAuthRepository.save(user);

        log.info("User registered successfully: {}", user.getEmail());
    }

    /**
     * Login User
     */
    @Override

    public LoginResponseDto userLogin(String email, String password) {
        Optional<UserEntity> optionalUser = usersAuthRepository.findByEmail(email);
        log.info("Fetched user: {}", optionalUser);

        if (optionalUser.isEmpty()) {
            log.info("Login failed: user not found for email {}", email);
            return new LoginResponseDto(null, "❌ No user found with this email");
        }

        UserEntity user = optionalUser.get();
        log.info("Stored hash: {}", user.getPassword());
        log.info("Raw input password: {}", password);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("Login failed: password mismatch");
            return new LoginResponseDto(null, "❌ Incorrect password");
        }

        log.info("Login success for {}", email);
        String token = jwtUtil.generateToken(email);
        return new LoginResponseDto(token, "✅ Login successful");
    }
}