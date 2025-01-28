package product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import product.exception.ProductNotFoundException;
import product.model.Product;
import product.model.ProductCategory;
import product.repository.ProductRepository;
import product.rest.RestUpdateProduct;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAllByCategory(ProductCategory.fromDisplayName(category));
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(String productId, RestUpdateProduct restUpdateProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

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
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
