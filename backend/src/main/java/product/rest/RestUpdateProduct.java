package product.rest;

public record RestUpdateProduct(
        String name,
        String description,
        Float price,
        String category
) {
}
