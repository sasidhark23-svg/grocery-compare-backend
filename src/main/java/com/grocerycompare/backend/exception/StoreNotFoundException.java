package com.grocerycompare.backend.exception;

public class StoreNotFoundException extends ResourceNotFoundException {
    public StoreNotFoundException(Long id) {
        super("Store not found with id: " + id);
    }

    public StoreNotFoundException(String message) {
        super(message);
    }
}
