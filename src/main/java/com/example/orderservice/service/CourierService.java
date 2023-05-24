package com.example.orderservice.service;

import com.example.orderservice.entity.CourierRequest;
import com.example.orderservice.entity.Role;
import com.example.orderservice.model.User;
import com.example.orderservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final PasswordEncoder encoder;
    private final UserRepo userRepo;

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

    public List<User> getAll() {
        return userRepo.findAllByRole(Role.COURIER);
    }
}
