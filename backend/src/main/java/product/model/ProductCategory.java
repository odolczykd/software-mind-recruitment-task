package product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import product.exception.InvalidProductCategoryException;

@RequiredArgsConstructor
@Getter
public enum ProductCategory {
    ELECTRONICS("Elektronika"),
    BOOKS("Książki"),
    OTHER("Inne");

    private final String displayName;

    public static ProductCategory fromDisplayName(String displayName) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }

        throw new InvalidProductCategoryException(displayName);
    }
}
