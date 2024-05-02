package com.example.customvault.services.impl;

import com.example.customvault.dto.TokenResponse;
import com.example.customvault.dto.User.UserLoginModel;
import com.example.customvault.models.User;
import com.example.customvault.security.JwtTokenProvider;
import com.example.customvault.services.AuthService;
import com.example.customvault.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public User create(User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Пользователь с таким username уже существует: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userService.createUser(user);
    }

    @Override
    public TokenResponse register(User user) {
        User createdUser = create(user);
        String accessToken = jwtTokenProvider.createAccessToken(createdUser.getId(), createdUser.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(createdUser.getId(), createdUser.getUsername());

        return new TokenResponse(createdUser.getId(), createdUser.getUsername(), accessToken, refreshToken);
    }

    @Override
    public TokenResponse login(UserLoginModel userLoginModel) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginModel.getUsername(), userLoginModel.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByUsername(userLoginModel.getUsername());

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername());

        return new TokenResponse(user.getId(), user.getUsername(), accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}



























