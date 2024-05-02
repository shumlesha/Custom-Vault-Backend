package com.example.customvault.exceptions;

public class NotSecretOwnerException extends RuntimeException {
    public NotSecretOwnerException(String message) {
        super(message);
    }
}
