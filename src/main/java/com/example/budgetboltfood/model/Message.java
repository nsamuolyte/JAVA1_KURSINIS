package com.example.budgetboltfood.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private SenderType senderType; // CLIENT / RESTAURANT / DRIVER

    @ManyToOne
    private User client;

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Cart order;
}
