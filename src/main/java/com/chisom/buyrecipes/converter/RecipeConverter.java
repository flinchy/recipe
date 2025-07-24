package com.chisom.buyrecipes.converter;

import com.chisom.buyrecipes.dto.ProductResponse;
import com.chisom.buyrecipes.dto.RecipeResponse;
import com.chisom.buyrecipes.entity.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class RecipeConverter implements Function<Recipe, RecipeResponse> {

    @Override
    public RecipeResponse apply(Recipe recipe) {
        return toResponse(recipe);
    }

    private RecipeResponse toResponse(Recipe recipe) {

        List<ProductResponse> products = recipe
                .getIngredients().stream().map(p ->
                        new ProductResponse(p.getProduct().getId(),
                                p.getProduct().getName(),
                                p.getProduct().getPriceIncents()))
                .toList();

        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                products
        );
    }
}
