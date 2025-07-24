package com.chisom.buyrecipes.dto;

import com.chisom.buyrecipes.enums.CartLine;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartItemResponse(Long id,
                               Long cartRecipeId,
                               CartLine cartLine,
                               Long quantity,
                               ProductResponse product,
                               RecipeResponse recipe) {
}
