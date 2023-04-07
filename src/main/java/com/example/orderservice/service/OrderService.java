package com.example.orderservice.service;

import com.example.orderservice.configuration.JwtAuthFilter;
import com.example.orderservice.entity.*;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.User;
import com.example.orderservice.repository.OrderRepo;
import com.example.orderservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final JwtAuthFilter filter;
    private final PasswordEncoder encoder;

    public OrderResponse changeDestination(
            ChangeDestRequest request) {
        Order order = getActiveOrder(request.orderId());
        order.setDestination(request.destination());
        orderRepo.save(order);
        return fillResponse(order);
    }

    public OrderResponse changeStatus(Integer orderId, String status) {
        Order order = getActiveOrder(orderId);
        order.setStatus(Status.valueOf(status));
        orderRepo.save(order);
        return fillResponse(order);
    }

    public OrderResponse createOrder(OrderRequest order) {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        Order new_order = Order.builder()
                .weight(order.weight())
                .courier(userRepo
                        .getCourier()
                        .orElseThrow())
                .user(user)
                .destination(order.destination())
                .status(Status.SHIPPED)
                .build();
        return fillResponse(orderRepo.save(new_order));
    }

    public Status cancelOrder(Integer orderId) {
        Order order = getActiveOrder(orderId);
        order.setStatus(Status.CANCELED);
        return orderRepo.save(order).getStatus();

    }


    private OrderResponse fillResponse(Order item) {
        return new OrderResponse(
                item.getId(),
                item.getWeight(),
                item.getDestination(),
                item.getDeparture(),
                item.getStatus(),
                item.getUser().getId(),
                item.getCourier().getId()
        );
    }

    public OrderResponse getOrder(Integer orderId) {
        return fillResponse(orderRepo
                .findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order with such id")));
    }


    private Order getActiveOrder(Integer orderId) {
        return orderRepo
                .findActiveById(orderId)
                .orElseThrow(() -> new NotFoundException("No courier with such id"));
    }

    public List<OrderResponse> getCourierOrders() {
        User courier = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        return orderRepo.findAllByCourier(courier)
                .stream()
                .map(this::fillResponse)
                .toList();
    }

    public List<OrderResponse> getUserOrders() {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        return orderRepo.findAllByUser(user)
                .stream()
                .map(this::fillResponse)
                .toList();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(this::fillResponse)
                .toList();
    }

    public OrderResponse assignOrder(
            Integer orderId,
            Integer courierId
    ) {
        User courier = userRepo.findById(courierId).orElseThrow();
        Order order = getActiveOrder(orderId);
        order.setCourier(courier);
        return fillResponse(orderRepo.save(order));
    }
    public List<User> getAllCouriers() {
       return userRepo.findAllByRole(Role.COURIER);
    }

    public Integer createCourier(CourierRequest request) {
        User user = User.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.COURIER)
                .name(request.name())
                .surname(request.surname())
                .build();
        return userRepo.save(user).getId();
    }
}
