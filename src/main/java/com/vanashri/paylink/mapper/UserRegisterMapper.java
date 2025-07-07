package com.vanashri.paylink.mapper;

import com.vanashri.paylink.dto.UserRegisterRequestDto;
import com.vanashri.paylink.entity.UserEntity;

public class UserRegisterMapper {

    public static UserEntity mapToEntity(UserRegisterRequestDto userRegisterRequestDto){
        UserEntity userEntity=UserEntity.builder()
                .userName(userRegisterRequestDto.getUserName())
                .email(userRegisterRequestDto.getEmail())
                .password(userRegisterRequestDto.getPassword())
                .role(userRegisterRequestDto.getRole())
                .build();

        return userEntity;

    }

}
