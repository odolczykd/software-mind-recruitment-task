package com.softwaremind.odolczykd.recruitment.product.service;

import com.softwaremind.odolczykd.recruitment.product.model.Product;
import com.softwaremind.odolczykd.recruitment.product.model.ProductCategory;
import com.softwaremind.odolczykd.recruitment.product.rest.RestUpdateProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    Optional<Product> getProductById(UUID id);

    Product updateProduct(UUID id, RestUpdateProduct restUpdateProduct);

    void deleteProduct(UUID id);
}
