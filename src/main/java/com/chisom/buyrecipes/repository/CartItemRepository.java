package com.chisom.buyrecipes.repository;

import com.chisom.buyrecipes.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteByCartRecipeId(Long cartRecipeId);

    Optional<CartItem> findByCartRecipeId(Long cartRecipeId);
}
