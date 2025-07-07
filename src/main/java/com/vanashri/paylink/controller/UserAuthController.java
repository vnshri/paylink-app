package com.vanashri.paylink.controller;

import com.vanashri.paylink.dto.LoginRequestDto;
import com.vanashri.paylink.dto.LoginResponseDto;
import com.vanashri.paylink.dto.UserRegisterRequestDto;
import com.vanashri.paylink.entity.UserEntity;
import com.vanashri.paylink.service.UserAuthService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@Slf4j
@RequestMapping("user")

public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    // store data in db
    @PostMapping("/store")
    public ResponseEntity<String> saveUser(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        log.info("Store User in DB");
        String message = "User Created Successfully With username: " + userRegisterRequestDto.getUserName();
        userAuthService.addUser(userRegisterRequestDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("User login attempt: {}", loginRequestDto.getEmail());

        LoginResponseDto response = userAuthService.userLogin(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()
        );

        if (response.getToken() == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}