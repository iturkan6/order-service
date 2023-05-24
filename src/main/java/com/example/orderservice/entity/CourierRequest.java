package com.example.orderservice.entity;

public record CourierRequest(
        String name,
        String surname,
        String email,
        String password
) { }
