package com.example.orderservice.configuration;


import com.example.orderservice.entity.Role;
import com.example.orderservice.model.User;
import com.example.orderservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAdminLoader implements ApplicationRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepo.findByEmail(System.getenv("ADMIN_EMAIL")).isEmpty()) {
            userRepo.save(User.builder()
                    .name("Admin")
                    .surname("Admin")
                    .email(System.getenv("ADMIN_EMAIL"))
                    .password(encoder.encode("ADMIN_PASSWORD"))
                    .role(Role.ADMIN)
                    .build());
        }
    }
}
