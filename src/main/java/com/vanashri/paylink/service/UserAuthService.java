package com.vanashri.paylink.service;

import com.vanashri.paylink.dto.LoginResponseDto;
import com.vanashri.paylink.dto.UserRegisterRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthService {

//store data
    public void addUser(UserRegisterRequestDto userRegisterRequestDto);

// Login User
    public LoginResponseDto userLogin(String email, String password);

}
