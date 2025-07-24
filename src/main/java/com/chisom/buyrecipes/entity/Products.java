package com.chisom.buyrecipes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
@SQLRestriction("deleted_at IS NULL") // Soft delete filter
public class Products extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "price_in_cents")
    private BigDecimal priceIncents;
}
