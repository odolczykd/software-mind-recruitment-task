package com.softwaremind.odolczykd.recruitment.product.controller;

import com.softwaremind.odolczykd.recruitment.product.rest.RestAddProduct;
import com.softwaremind.odolczykd.recruitment.product.rest.RestProduct;
import com.softwaremind.odolczykd.recruitment.product.rest.RestProductDetails;
import com.softwaremind.odolczykd.recruitment.product.rest.RestUpdateProduct;
import com.softwaremind.odolczykd.recruitment.product.rest.mapper.ProductMapper;
import com.softwaremind.odolczykd.recruitment.product.service.ProductServiceImpl;
import com.softwaremind.odolczykd.recruitment.shared.rest.ErrorMessagesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceImpl productService;
    private final ProductMapper productMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody RestAddProduct newProduct,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ErrorMessagesResponse.create(errors));
        }
        var product = productMapper.toNewProduct(newProduct);
        final var createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toRestProduct(createdProduct));
    }

    @GetMapping
    public List<RestProduct> getProducts() {
        return productService.getAllProducts()
                .stream()
                .map(productMapper::toRestProduct)
                .toList();
    }

    @GetMapping("/category/{category}")
    public List<RestProduct> getProductsByCategory(@PathVariable("category") String category) {
        return productService.getProductsByCategory(category)
                .stream()
                .map(productMapper::toRestProduct)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestProductDetails> getProductDetails(@PathVariable("id") UUID productId) {
        return productService.getProductById(productId)
                .map(product -> ResponseEntity.ok(productMapper.toRestProductDetails(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RestProductDetails> updateProduct(@PathVariable("id") UUID productId,
                                                            @RequestBody RestUpdateProduct restUpdateProduct) {
        var updatedProduct = productService.updateProduct(productId, restUpdateProduct);
        return ResponseEntity.ok(productMapper.toRestProductDetails(updatedProduct));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") UUID productId) {
        var product = productService.getProductById(productId);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
