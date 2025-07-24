package com.chisom.buyrecipes.service;

import com.chisom.buyrecipes.converter.RecipeConverter;
import com.chisom.buyrecipes.dto.PageResponse;
import com.chisom.buyrecipes.dto.RecipeResponse;
import com.chisom.buyrecipes.entity.Recipe;
import com.chisom.buyrecipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.chisom.buyrecipes.util.BuyRecipeUtil.realPage;
import static com.chisom.buyrecipes.util.BuyRecipeUtil.wrap;

@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final int MAX_PAGE_SIZE = 5;
    private final RecipeConverter recipeConverter;
    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public PageResponse<RecipeResponse> getRecipes(int page) {

        Pageable pageable = PageRequest.of(realPage(page), MAX_PAGE_SIZE);

        Page<Recipe> recipes = wrap(
                // 1. action: load the entity
                () -> recipeRepository.findAllRecipe(pageable),
                // 2. onError: send an alert or rollback some in-memory cache
                () -> logger.warn("Failed to load recipe"),
                // 3. status
                HttpStatus.INTERNAL_SERVER_ERROR,
                // 4. client-facing message
                "Could not retrieve recipe at this time"
        );

        return PageResponse.from(recipes, recipeConverter);
    }
}
