package com.example.budgetboltfood.model;

public enum OrderStatus {
    PENDING,        // užsakymas gautas
    ACCEPTED,       // patvirtino restoranas
    IN_PROGRESS,    // ruošiama
    READY,          // paruošta atsiėmimui
    ON_THE_WAY,     // kurjeris veža
    DELIVERED,      // pristatytas
    CANCELLED       // atšauktas
}
