package product.rest;

public record RestProduct(
        String name,
        String description,
        float price,
        String category
) {
}
