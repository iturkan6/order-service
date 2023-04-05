package com.example.orderservice.entity;

public record OrderResponse(
        Integer id,
        String weight,
        String destination,
        String departure,
        Status status,
        Integer userId,
        Integer courierId) {
}
