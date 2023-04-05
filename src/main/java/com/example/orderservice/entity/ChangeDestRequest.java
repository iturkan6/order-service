package com.example.orderservice.entity;

public record ChangeDestRequest(
        Integer orderId,
        String destination
) {}
