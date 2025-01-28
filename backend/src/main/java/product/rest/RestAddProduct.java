package product.rest;

public record RestAddProduct(
        String name,
        String description,
        float price,
        String category
) {
}
