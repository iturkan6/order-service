package com.example.orderservice.model;

import com.example.orderservice.entity.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_order")
public class Order {
    @Id
    private Integer id;
    private String weight;
    private String departure;
    private String destination;

    @Enumerated(value = EnumType.STRING)
    Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;
}
