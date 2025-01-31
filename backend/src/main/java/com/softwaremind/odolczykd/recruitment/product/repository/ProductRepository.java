package com.softwaremind.odolczykd.recruitment.product.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import com.softwaremind.odolczykd.recruitment.product.model.Product;
import com.softwaremind.odolczykd.recruitment.product.model.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CassandraRepository<Product, UUID> {
    List<Product> findAllByCategory(ProductCategory category);
}
