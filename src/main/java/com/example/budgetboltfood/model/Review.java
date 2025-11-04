package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class Review
{
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
