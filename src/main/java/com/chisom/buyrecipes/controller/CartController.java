package com.chisom.buyrecipes.controller;

import com.chisom.buyrecipes.dto.CartRecipeRequest;
import com.chisom.buyrecipes.dto.CartResponse;
import com.chisom.buyrecipes.service.CartResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("carts")
@RequiredArgsConstructor
public class CartController {

    private final CartResourceService service;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse> getCartById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getCartById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/add_recipe", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addRecipeToCart(
            @PathVariable("id") Long id, @RequestBody @Valid CartRecipeRequest request
    ) {
        service.addRecipe(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/recipes/{cartRecipeId}")
    public ResponseEntity<Void> removeRecipe(
            @PathVariable("cartId") Long cartId, @PathVariable("cartRecipeId") Long cartRecipeId
    ) {
        service.removeRecipe(cartId, cartRecipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
