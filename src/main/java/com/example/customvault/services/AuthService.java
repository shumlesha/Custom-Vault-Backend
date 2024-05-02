package com.example.customvault.services;

import com.example.customvault.dto.TokenResponse;
import com.example.customvault.dto.User.UserLoginModel;
import com.example.customvault.models.User;

public interface AuthService {
    TokenResponse register(User user);

    TokenResponse login(UserLoginModel userLoginModel);

    TokenResponse refreshToken(String refreshToken);
}
