package com.chisom.buyrecipes.service;

import com.chisom.buyrecipes.dto.PageResponse;
import com.chisom.buyrecipes.dto.RecipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeResourceService {

    private final RecipeService recipeService;

    public PageResponse<RecipeResponse> getRecipes(int page) {
        return recipeService.getRecipes(page);
    }
}
