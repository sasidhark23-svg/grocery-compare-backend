package com.grocerycompare.backend.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("User already exists with email: " + email);
    }
}
