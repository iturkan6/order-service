package com.example.orderservice.controller;

import com.example.orderservice.entity.AssignOrderRequest;
import com.example.orderservice.entity.ChangeStatusRequest;
import com.example.orderservice.entity.CourierRequest;
import com.example.orderservice.entity.OrderResponse;
import com.example.orderservice.model.User;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "BasicAuth")
@RequiredArgsConstructor
public class AdminController {
    private final OrderService service;

    @PostMapping("/status")
    public ResponseEntity<OrderResponse> changeStatus(
            @RequestBody ChangeStatusRequest request
    ) {
        return ResponseEntity.ok(service.changeStatus(
                request.orderId(),
                request.status()));
    }

    @PostMapping("/courier")
    public ResponseEntity<Integer> createCourier(
            @RequestBody CourierRequest request
    ) {
        return new ResponseEntity<>(service.createCourier(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrdersInfo() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @PostMapping("/assign")
    public ResponseEntity<OrderResponse> assignOrder(
            @RequestBody AssignOrderRequest request
    ) {
        return ResponseEntity.ok(service.assignOrder(
                request.orderId(),
                request.courierId()));
    }

    @GetMapping("/couriers")
    public ResponseEntity<List<User>> getAllCouriers() {
        return ResponseEntity.ok(service.getAllCouriers());
    }

}
