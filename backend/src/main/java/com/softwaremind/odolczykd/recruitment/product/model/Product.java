package com.softwaremind.odolczykd.recruitment.product.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("products")
@Getter
@Setter
@Builder
public class Product {
    @PrimaryKey
    private UUID id;

    private String name;
    private String description;
    private float price;

    @Column("category")
    private ProductCategory category;
}
