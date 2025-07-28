package com.chisom.buyrecipes.unit;

import com.chisom.buyrecipes.converter.RecipeConverter;
import com.chisom.buyrecipes.dto.RecipeResponse;
import com.chisom.buyrecipes.entity.Products;
import com.chisom.buyrecipes.entity.Recipe;
import com.chisom.buyrecipes.entity.RecipeIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeConverterTest {

    private RecipeConverter recipeConverter;

    @BeforeEach
    void setUp() {
        recipeConverter = new RecipeConverter();
    }

    @Test
    void convertToRecipeResponse() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pesto Pasta");
        recipe.setDescription("Spaghetti tossed with basil pesto, parmesan and olive oil");

        Products product = new Products();
        product.setId(1L);
        product.setName("Fresh Basil 50g");
        product.setPriceIncents(new BigDecimal("249.00"));


        Set<RecipeIngredient> ingredients = new HashSet<>();
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setId(1L);
        ingredient.setRecipe(recipe);
        ingredient.setProduct(product);
        ingredients.add(ingredient);

        recipe.setIngredients(ingredients);

        RecipeResponse response = recipeConverter.apply(recipe);

        assertEquals(recipe.getId(), response.id());
        assertEquals(recipe.getName(), response.name());
        assertEquals(recipe.getDescription(), response.description());
        assertEquals(recipe.getIngredients().stream().toList().getFirst().getId(),
                response.ingredient().getFirst().id());
    }
}
