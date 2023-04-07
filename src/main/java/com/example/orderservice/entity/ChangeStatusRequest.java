package com.example.orderservice.entity;

public record ChangeStatusRequest(
        Integer orderId,
        String status
) {}
