package product.service;

import product.model.Product;
import product.rest.RestAddProduct;
import product.rest.RestUpdateProduct;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void createProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    Optional<Product> getProductById(String id);

    Product updateProduct(String productId, RestUpdateProduct restUpdateProduct);

    void deleteProduct(String id);
}
