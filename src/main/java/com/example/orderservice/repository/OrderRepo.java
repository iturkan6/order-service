package com.example.orderservice.repository;


import com.example.orderservice.model.Order;
import com.example.orderservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

//    @Query("select ord from Order ord where ord.id = ?1 and ord.status = 'SHIPPED'")
//    Optional<Order> findActiveById(Integer id);

//    @Query("select ord from Order ord where ord.status = 'SHIPPED'")
//    List<Order> findAllActive();

    Optional<Order> findByIsActiveIsTrueAndId(Integer id);

    List<Order> findAllByUser(User user);
    List<Order> findAllByCourier(User courier);
}
