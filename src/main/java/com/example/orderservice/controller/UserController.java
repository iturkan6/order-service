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

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest order) {
        return new ResponseEntity<>(service.createOrder(order), HttpStatus.CREATED);
    }

    @PostMapping("/order/destination")
    public ResponseEntity<OrderResponse> changeDestination
            (@RequestBody ChangeDestRequest order) {
        return ResponseEntity.ok(service.changeOrderDestination(order));
    }

    @PostMapping("/order/{id}/cancel")
    public ResponseEntity<Status> changeDestination
            (@PathVariable(name = "id") Integer orderId) {
        return ResponseEntity.ok(service.cancelOrder(orderId));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrderInfo
            (@PathVariable Integer id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersInfo() {
        return ResponseEntity.ok(service.getOrdersByUser());
    }
}
