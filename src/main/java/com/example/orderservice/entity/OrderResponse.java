package com.example.orderservice.entity;

public record OrderResponse(
        Integer id,
        Double weight,
        String destination,
        String departure,
        Status status,
        String userId,
        String courierId) {
}
