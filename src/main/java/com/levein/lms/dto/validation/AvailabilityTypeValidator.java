package com.levein.lms.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class AvailabilityTypeValidator implements ConstraintValidator<ValidateAvailabilityType,String> {
    @Override
    public boolean isValid(String availabilityType, ConstraintValidatorContext constraintValidatorContext) {

        List<String>availabilityTypes= Arrays.asList("AVAILABLE","RESERVED");
         return availabilityTypes.contains(availabilityType);
    }
}
