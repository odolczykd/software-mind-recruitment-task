package com.softwaremind.odolczykd.recruitment.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductCategory {
    ELECTRONICS("Elektronika"),
    BOOKS("Książki"),
    PRESS("Prasa"),
    MUSIC("Muzyka"),
    MOVIES("Filmy"),
    OTHER("Inne");

    private final String displayName;

    public static ProductCategory fromDisplayName(String displayName) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return null;
    }
}
