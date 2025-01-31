package com.softwaremind.odolczykd.recruitment.product.rest;

import com.softwaremind.odolczykd.recruitment.product.rest.category.ValidCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RestAddProduct(
        @NotEmpty(message = "Product name cannot be empty")
        String name,

        @NotNull(message = "Product name cannot be null")
        String description,

        @DecimalMin(value = "0.0", message = "Product price cannot be a negative value")
        float price,

        @ValidCategory(message = "Product category is invalid")
        String category
) {
}
