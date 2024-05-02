package com.example.customvault.repository;

import com.example.customvault.models.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, UUID> {
}
