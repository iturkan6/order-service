package com.example.orderservice.controller;

import com.example.orderservice.entity.ChangeStatusRequest;
import com.example.orderservice.entity.OrderResponse;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courier")
@SecurityRequirement(name = "BasicAuth")
@RequiredArgsConstructor
public class CourierController {

    private final OrderService service;

    @GetMapping("/")
    public ResponseEntity<List<OrderResponse>> getAllOrdersInfo() {
        return ResponseEntity.ok(service.getCourierOrders());
    }

    @PostMapping("/status")
    public ResponseEntity<OrderResponse> changeStatus(
            @RequestBody ChangeStatusRequest request
    ) {
        return ResponseEntity.ok(service.changeStatus(
                request.orderId(),
                request.status()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderInfo
            (@PathVariable Integer id) {
        return ResponseEntity.ok(service.getOrder(id));
    }
}
