package com.example.orderservice.entity;

public record OrderRequest(
        String weight,
        String destination,
        String departure) {
}
