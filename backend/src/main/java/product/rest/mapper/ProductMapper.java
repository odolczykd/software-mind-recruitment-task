package product.rest.mapper;

import org.springframework.stereotype.Component;
import product.model.Product;
import product.model.ProductCategory;
import product.rest.RestAddProduct;
import product.rest.RestProduct;
import product.rest.RestProductDetails;

@Component
public final class ProductMapper {
    public Product toProduct(RestAddProduct restAddProduct) {
        return Product.builder()
                .name(restAddProduct.name())
                .description(restAddProduct.description())
                .price(restAddProduct.price())
                .category(ProductCategory.fromDisplayName(restAddProduct.category()))
                .build();
    }

    public RestProduct toRestProduct(Product product) {
        return new RestProduct(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getDisplayName()
        );
    }

    public RestProductDetails toRestProductDetails(Product product) {
        return new RestProductDetails(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getDisplayName()
        );
    }
}
