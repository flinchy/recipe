package com.chisom.buyrecipes.service;

import com.chisom.buyrecipes.dto.CartRecipeRequest;
import com.chisom.buyrecipes.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartResourceService {

    private final CartService cartService;

    public CartResponse getCartById(Long id) {
        return cartService.getCartItem(id);
    }

    public void addRecipe(Long cartId, CartRecipeRequest request) {
        cartService.addRecipe(cartId, request);
    }

    public void removeRecipe(Long cartId, Long recipeId) {
        cartService.deleteRecipeFromCart(cartId, recipeId);
    }
}
