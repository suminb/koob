package io.shortbread.koob.models;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DatetimeRangeValidator.class })
public @interface ValidDatetimeRange {
    String message() default "Value is out of range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;
}