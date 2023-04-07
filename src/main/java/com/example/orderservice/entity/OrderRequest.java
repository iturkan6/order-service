package com.example.orderservice.entity;

public record OrderRequest(
        Double weight,
        String destination,
        String departure) {
}
