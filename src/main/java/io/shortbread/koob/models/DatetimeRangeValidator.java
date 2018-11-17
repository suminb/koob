package io.shortbread.koob.models;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DatetimeRangeValidator implements ConstraintValidator<ValidDatetimeRange, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidDatetimeRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || (value >= min && value <= max);
    }
}
