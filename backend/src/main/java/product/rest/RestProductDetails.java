package product.rest;

public record RestProductDetails(
        String name,
        String description,
        float price,
        String category
) {
}
