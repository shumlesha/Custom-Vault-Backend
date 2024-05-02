package com.example.customvault.services;


import com.example.customvault.models.User;

import java.util.UUID;

public interface UserService {
    User getById(UUID userId);

    User findByUsername(String username);

    User createUser(User user);

    boolean existsByUsername(String username);

    User getByUsername(String username);
}
