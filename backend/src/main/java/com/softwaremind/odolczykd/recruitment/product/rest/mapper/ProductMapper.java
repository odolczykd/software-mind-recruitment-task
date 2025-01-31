package com.softwaremind.odolczykd.recruitment.product.rest.mapper;

import org.springframework.stereotype.Component;
import com.softwaremind.odolczykd.recruitment.product.model.Product;
import com.softwaremind.odolczykd.recruitment.product.model.ProductCategory;
import com.softwaremind.odolczykd.recruitment.product.rest.RestAddProduct;
import com.softwaremind.odolczykd.recruitment.product.rest.RestProduct;
import com.softwaremind.odolczykd.recruitment.product.rest.RestProductDetails;

import java.util.UUID;

@Component
public class ProductMapper {
    public Product toNewProduct(RestAddProduct restAddProduct) {
        return Product.builder()
                .id(UUID.randomUUID())
                .name(restAddProduct.name())
                .description(restAddProduct.description())
                .price(restAddProduct.price())
                .category(ProductCategory.fromDisplayName(restAddProduct.category()))
                .build();
    }

    public RestProduct toRestProduct(Product product) {
        return new RestProduct(
                product.getId(),
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
