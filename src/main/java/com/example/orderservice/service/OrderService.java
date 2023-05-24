package com.example.orderservice.service;

import com.example.orderservice.configuration.JwtAuthFilter;
import com.example.orderservice.entity.*;
import com.example.orderservice.exception.ActiveCourierNotFoundException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.User;
import com.example.orderservice.repository.OrderRepo;
import com.example.orderservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final JwtAuthFilter filter;
    private final KafkaTemplate<String, KafkaDTO> kafkaTemplate;

    public OrderResponse changeOrderDestination(
            ChangeDestRequest request) {
        Order order = getActiveOrder(request.orderId());
        order.setDestination(request.destination());
        orderRepo.save(order);
        return fillResponse((order));
    }

    public OrderResponse changeOrderStatus(ChangeStatusRequest request) {
        Order order = getActiveOrder(request.orderId());
        order.setStatus(Status.valueOf(request.status()));
        if (Status.DELIVERED.name().equals(request.status())) {
            order.setIsActive(Boolean.FALSE);
        }
        kafkaTemplate.send("user_status", new KafkaDTO(order.getUser().getId(), request.status()));
        return fillResponse((order));
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();

        log.info("Creating order for user with id {}", user.getId());

        OrderResponse order = fillResponse(
                orderRepo.save(
                        Order.builder()
                                .weight(orderRequest.weight())
                                .user(user)
                                .destination(orderRequest.destination())
                                .departure(orderRequest.departure())
                                .status(Status.CREATED)
                                .build()));
//        kafkaTemplate.send("user_status", new KafkaDTO(user.getId(), Status.CREATED.name()));

        User courier = getActiveCourier();
        if (courier != null) {
            order = assignOrder(order.id(), courier.getId());
        }
        return order;
    }

    public Status cancelOrder(Integer orderId) {
        Order order = getActiveOrder(orderId);
        order.setStatus(Status.CANCELLED);
        order.setIsActive(Boolean.FALSE);
        kafkaTemplate.send("user_status", new KafkaDTO(order.getUser().getId(), Status.CANCELLED.name()));
        return orderRepo.save(order).getStatus();
    }

    public OrderResponse getOrderById(Integer orderId) {
        return fillResponse((orderRepo
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId))));
    }

    public List<OrderResponse> getOrdersByUser() {
        User user = userRepo.findByEmail(filter.getEmail()).orElseThrow();
        var orders = user.getRole().equals(Role.USER) ?
                orderRepo.findAllByUser(user) :
                orderRepo.findAllByCourier(user);
        return orders.stream().map(this::fillResponse).toList();
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
        User courier = getUserById(courierId);
        Order order = getActiveOrder(orderId);
        order.setCourier(courier);
        order.setStatus(Status.SHIPPED);
//        kafkaTemplate.send("user_status", new KafkaDTO(order.getUser().getId(), Status.SHIPPED.name()));
        return fillResponse((orderRepo.save(order)));
    }

    private Order getActiveOrder(Integer orderId) {
        return orderRepo
                .findByIsActiveIsTrueAndId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private OrderResponse fillResponse(Order item) {
        var courierId = item.getCourier() != null ?
                item.getCourier().getId().toString() :
                null;
        return new OrderResponse(
                item.getId(),
                item.getWeight(),
                item.getDestination(),
                item.getDeparture(),
                item.getStatus(),
                item.getUser().getId().toString(),
                courierId
        );
    }

    private User getUserById(Integer id) {
        return userRepo.findById(id).orElseThrow();
    }

    private User getActiveCourier() {
        return userRepo.findAllByRole(Role.COURIER).stream().findAny().orElseThrow(ActiveCourierNotFoundException::new);
    }

}
