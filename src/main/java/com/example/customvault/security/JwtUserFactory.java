package com.example.customvault.security;


import com.example.customvault.models.User;

public class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }

}
