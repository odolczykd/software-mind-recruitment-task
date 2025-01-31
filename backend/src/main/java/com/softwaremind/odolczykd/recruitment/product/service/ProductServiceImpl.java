package com.softwaremind.odolczykd.recruitment.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.softwaremind.odolczykd.recruitment.product.exception.ProductNotFoundException;
import com.softwaremind.odolczykd.recruitment.product.model.Product;
import com.softwaremind.odolczykd.recruitment.product.model.ProductCategory;
import com.softwaremind.odolczykd.recruitment.product.repository.ProductRepository;
import com.softwaremind.odolczykd.recruitment.product.rest.RestUpdateProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final List<Product> EMPTY_LIST = List.of();

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        var categoryEnum = ProductCategory.fromDisplayName(category);
        if (categoryEnum == null) {
            return EMPTY_LIST;
        }
        return productRepository.findAllByCategory(categoryEnum);
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(UUID id, RestUpdateProduct restUpdateProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id.toString()));

        if (restUpdateProduct.name() != null) {
            existingProduct.setName(restUpdateProduct.name());
        }
        if (restUpdateProduct.description() != null) {
            existingProduct.setDescription(restUpdateProduct.description());
        }
        if (restUpdateProduct.price() != null) {
            existingProduct.setPrice(restUpdateProduct.price());
        }
        if (restUpdateProduct.category() != null) {
            existingProduct.setCategory(ProductCategory.fromDisplayName(restUpdateProduct.category()));
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
