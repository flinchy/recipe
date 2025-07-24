package com.chisom.buyrecipes.entity;

import com.chisom.buyrecipes.enums.CartLine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL") // Soft delete filter
@NoArgsConstructor
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_recipe_id")
    private CartRecipe cartRecipe;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "line_type")
    private CartLine lineType;  //default 'PRODUCT'

    @Column(name = "quantity")
    private Long quantity;

    public CartItem(Cart cart, CartRecipe cartRecipe, CartLine lineType, Long quantity) {
        this.cart = cart;
        this.cartRecipe = cartRecipe;
        this.lineType = lineType;
        this.quantity = quantity;
    }
}