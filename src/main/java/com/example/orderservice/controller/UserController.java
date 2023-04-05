package com.example.orderservice.controller;

import com.example.orderservice.entity.ChangeDestRequest;
import com.example.orderservice.entity.OrderRequest;
import com.example.orderservice.entity.OrderResponse;
import com.example.orderservice.entity.Status;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "BasicAuth")
@RequiredArgsConstructor
public class UserController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest order) {
        return new ResponseEntity<>(service.createOrder(order), HttpStatus.CREATED);
    }

    @PostMapping("/destination")
    public ResponseEntity<OrderResponse> changeDestination
            (@RequestBody ChangeDestRequest order) {
        return ResponseEntity.ok(service.changeDestination(order));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Status> changeDestination
            (@RequestBody Integer orderId) {
        return ResponseEntity.ok(service.cancelOrder(orderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderInfo
            (@PathVariable Integer id) {
        return ResponseEntity.ok(service.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrdersInfo() {
        return ResponseEntity.ok(service.getUserOrders());
    }
}
