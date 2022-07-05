package com.project.simplegw.system.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<DateValid, String> {

    @Override
    public void initialize(DateValid constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return true;
        
        try {   // 날짜 형식과 올바르지 않은 날짜를 필터링
            LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch(DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
