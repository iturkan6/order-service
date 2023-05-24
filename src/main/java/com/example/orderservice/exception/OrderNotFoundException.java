package com.example.orderservice.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Integer id) {
        super(String.format("Not found order with id -> %d", id));
    }
}
