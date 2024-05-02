package com.example.customvault.controller;


import com.example.customvault.dto.TokenResponse;
import com.example.customvault.dto.User.UserLoginModel;
import com.example.customvault.dto.User.UserRegisterModel;
import com.example.customvault.mappers.UserMapper;
import com.example.customvault.models.User;
import com.example.customvault.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts")
public class AccountController {

    private final AuthService authService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<TokenResponse> register(@Validated @RequestBody UserRegisterModel userRegisterModel) {
        User user = userMapper.toEntity(userRegisterModel);
        TokenResponse tokenResponse = authService.register(user);
        log.info("token created");
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<TokenResponse> login(@Validated @RequestBody UserLoginModel userLoginModel) {

        return ResponseEntity.ok(authService.login(userLoginModel));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
