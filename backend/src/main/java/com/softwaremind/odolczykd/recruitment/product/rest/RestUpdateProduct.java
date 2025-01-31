package com.softwaremind.odolczykd.recruitment.product.rest;

public record RestUpdateProduct(
        String name,
        String description,
        Float price,
        String category
) {
}
