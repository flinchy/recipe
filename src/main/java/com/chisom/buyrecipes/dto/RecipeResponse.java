package com.chisom.buyrecipes.dto;

import java.util.List;

public record RecipeResponse(Long id,
                             String name,
                             String description,
                             List<ProductResponse> ingredient) {
}
