package com.example.budgetboltfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int reviewId;
    protected double stars;
    protected String comment;
    protected TargetType targetType;
    protected int targetId;
    protected int userId;
    protected LocalDate dateCreated;

    public Review( double stars, String comment, TargetType targetType, int targetId, int userId) {
        this.stars = stars;
        this.comment = comment;
        this.targetType = targetType;
        this.targetId = targetId;
        this.userId = userId;
    }
}
