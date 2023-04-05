package com.example.orderservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

    //Just a method for proving of working kafka
    //todo realize kafka for notification ms
    @KafkaListener(id = "listenGroupFoo", topics = "user_token")
    public String listenGroupFoo(String message) {
        System.out.println("=====");
        System.out.println("Received Message in group foo: " + message);
        System.out.println("=====");
        return message;
    }
}
