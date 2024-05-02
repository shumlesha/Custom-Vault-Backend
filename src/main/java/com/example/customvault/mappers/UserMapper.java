package com.example.customvault.mappers;

import com.example.customvault.dto.User.UserRegisterModel;
import com.example.customvault.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRegisterModel toDTO(User user);

    User toEntity(UserRegisterModel dto);
}
