package product.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("products")
@Getter
@Setter
@Builder
public class Product {
    @Id
    @PrimaryKey
    private String id;

    private String name;
    private String description;
    private float price;
    private ProductCategory category;
}
