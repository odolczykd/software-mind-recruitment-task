package product.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import product.model.Product;
import product.model.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CassandraRepository<Product, String> {
    List<Product> findAllByCategory(ProductCategory category);
}
