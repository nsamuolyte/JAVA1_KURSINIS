package com.example.budgetboltfood.utils;

import java.time.LocalDate;

public class Validator {

    public static boolean isAtLeast16(LocalDate date) {
        if (date == null) return false;

        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(16);

        return !date.isAfter(today) && date.isBefore(minDate.plusDays(1));
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9]{8,15}$");
    }

    public static boolean isValidBirthDate2(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

}
