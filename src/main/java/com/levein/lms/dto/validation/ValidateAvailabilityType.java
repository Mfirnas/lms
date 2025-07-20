package com.levein.lms.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AvailabilityTypeValidator.class)
public @interface ValidateAvailabilityType {

    public String message() default "Invalid Availability Type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
