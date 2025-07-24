package com.chisom.buyrecipes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "recipe_ingredient",  uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "product_id"}))
@SQLRestriction("deleted_at IS NULL") // Soft delete filter
public class RecipeIngredient extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;
}
