package com.softwaremind.odolczykd.recruitment.product.rest.category;

import com.softwaremind.odolczykd.recruitment.product.model.ProductCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductCategoryValidator implements ConstraintValidator<ValidCategory, String> {
    @Override
    public void initialize(ValidCategory constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            ProductCategory category = ProductCategory.fromDisplayName(value);
            return category != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
