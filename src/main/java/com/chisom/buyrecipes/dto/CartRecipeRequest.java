package com.chisom.buyrecipes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartRecipeRequest(@NotNull(message = "Missing required field recipeId") Long recipeId,
                                @NotNull(message = "Missing required field quantity") @Min(1) Long quantity) {
}
