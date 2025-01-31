package com.softwaremind.odolczykd.recruitment.product.rest;

import java.util.UUID;

public record RestProduct(
        UUID id,
        String name,
        String description,
        float price,
        String category
) {
}
