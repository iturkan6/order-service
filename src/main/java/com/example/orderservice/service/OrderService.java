package com.example.orderservice.service;

import com.example.orderservice.configuration.JwtAuthFilter;
import com.example.orderservice.entity.*;
import com.example.orderservice.model.KafkaModel;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.User;
import com.example.orderservice.repository.OrderRepo;
import com.example.orderservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final JwtAuthFilter filter;
    private final PasswordEncoder encoder;
    private final KafkaTemplate<String, KafkaModel> kafkaTemplate;

    public OrderResponse changeDestination(
            ChangeDestRequest request) {
        Order order = getActiveOrder(request.orderId());
        order.setDestination(request.destination());
        orderRepo.save(order);
        return fillResponse(order);
    }

    public OrderResponse changeStatus(Integer orderId, String status) {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        Order order = getActiveOrder(orderId);
        order.setStatus(Status.valueOf(status));
        Order updated_status = orderRepo.save(order);
        kafkaTemplate.send("user_status", new KafkaModel(user.getEmail(), updated_status.getStatus().toString()));
        return fillResponse(order);
    }

    public OrderResponse createOrder(OrderRequest order) {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        log.info("Creating order for user with id {}", user.getId());
        Order new_order = Order.builder()
                .id(15)
                .weight(order.weight())
                .courier(userRepo
                        .getCourier()
                        .orElseThrow())
                .user(user)
                .destination(order.destination())
                .status(Status.SHIPPED)
                .build();

        kafkaTemplate.send("user_status", new KafkaModel(user.getEmail(), Status.SHIPPED.toString()));
        return fillResponse(orderRepo.save(new_order));
    }

    public Status cancelOrder(Integer orderId) {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        Order order = getActiveOrder(orderId);
        order.setStatus(Status.CANCELED);
        kafkaTemplate.send("user_status", new KafkaModel(user.getEmail(), Status.CANCELED.toString()));
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
