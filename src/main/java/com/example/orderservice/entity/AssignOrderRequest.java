package com.example.orderservice.entity;

public record AssignOrderRequest(
        Integer orderId,
        Integer courierId
) {}
