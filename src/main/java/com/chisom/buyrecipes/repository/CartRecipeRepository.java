package com.chisom.buyrecipes.repository;

import com.chisom.buyrecipes.entity.CartRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRecipeRepository extends JpaRepository<CartRecipe, Long> {

    void deleteByCartIdAndRecipeId(Long cartId, Long recipeId);
}
