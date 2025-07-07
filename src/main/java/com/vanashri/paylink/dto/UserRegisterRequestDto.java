package com.vanashri.paylink.dto;

import com.vanashri.paylink.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRegisterRequestDto {
    private String userName;
    private String email;
    private String password;
    private Role role;

}