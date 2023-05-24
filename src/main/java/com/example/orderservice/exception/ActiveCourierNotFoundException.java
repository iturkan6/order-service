package com.example.orderservice.exception;

public class ActiveCourierNotFoundException extends RuntimeException{
    public ActiveCourierNotFoundException() {
        super("No any active courier");
    }
}
