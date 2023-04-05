package com.example.orderservice.repository;


import com.example.orderservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.role = 'COURIER'")
    Optional<User> getCourier();
}
