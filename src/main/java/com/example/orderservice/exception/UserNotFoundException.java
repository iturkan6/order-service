package com.example.orderservice.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Integer id) {
        super(String.format("No user with id -> %d", id));
    }
}
