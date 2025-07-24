package com.chisom.buyrecipes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "cart_recipes")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE cart_recipes SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL") // Soft delete filter
public class CartRecipe extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public CartRecipe(Cart cart, Recipe recipe) {
        this.cart = cart;
        this.recipe = recipe;
    }
}
