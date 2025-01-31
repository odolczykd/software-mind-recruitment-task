package com.softwaremind.odolczykd.recruitment.product.rest.category;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProductCategoryValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategory {

    String message() default "Invalid category";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
