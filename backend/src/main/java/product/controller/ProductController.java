package product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import product.rest.RestAddProduct;
import product.rest.RestUpdateProduct;
import product.rest.mapper.ProductMapper;
import product.rest.RestProduct;
import product.rest.RestProductDetails;
import product.service.ProductServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final ProductServiceImpl productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<String> addNewProduct(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                @RequestBody RestAddProduct newProduct) {
        var product = productMapper.toProduct(newProduct);
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<RestProduct> getProducts(@RequestHeader(AUTHORIZATION_HEADER) String token) {
        return productService.getAllProducts()
                .stream()
                .map(productMapper::toRestProduct)
                .toList();
    }

    @GetMapping("/category/{category}")
    public List<RestProduct> getProductsByCategory(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                   @PathVariable("category") String category) {
        return productService.getProductsByCategory(category)
                .stream()
                .map(productMapper::toRestProduct)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestProductDetails> getProductDetails(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                                @PathVariable("id") String productId) {
        return productService.getProductById(productId)
                .map(product -> ResponseEntity.ok(productMapper.toRestProductDetails(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<RestProductDetails> updateProduct(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                            @PathVariable("id") String productId,
                                                            @RequestBody RestUpdateProduct restUpdateProduct) {
        var updatedProduct = productService.updateProduct(productId, restUpdateProduct);
        return ResponseEntity.ok(productMapper.toRestProductDetails(updatedProduct));
    }

    @DeleteMapping("/{id")
    public ResponseEntity<String> deleteProduct(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                @PathVariable("id") String productId) {
        var product = productService.getProductById(productId);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
