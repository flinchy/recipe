package com.chisom.buyrecipes.controller;

import com.chisom.buyrecipes.dto.PageResponse;
import com.chisom.buyrecipes.dto.RecipeResponse;
import com.chisom.buyrecipes.service.RecipeResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeResourceService service;

    @GetMapping(value = "recipes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<RecipeResponse>> getAllRecipes(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        return new ResponseEntity<>(service.getRecipes(page), HttpStatus.OK);
    }
}
