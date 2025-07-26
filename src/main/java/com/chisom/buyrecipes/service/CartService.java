package com.chisom.buyrecipes.service;

import com.chisom.buyrecipes.converter.ProductConverter;
import com.chisom.buyrecipes.converter.RecipeConverter;
import com.chisom.buyrecipes.dto.*;
import com.chisom.buyrecipes.entity.CartItem;
import com.chisom.buyrecipes.entity.CartRecipe;
import com.chisom.buyrecipes.entity.Cart;
import com.chisom.buyrecipes.entity.Recipe;
import com.chisom.buyrecipes.enums.CartLine;
import com.chisom.buyrecipes.enums.OperationType;
import com.chisom.buyrecipes.exception.BuyRecipeException;
import com.chisom.buyrecipes.repository.CartItemRepository;
import com.chisom.buyrecipes.repository.CartRecipeRepository;
import com.chisom.buyrecipes.repository.CartRepository;
import com.chisom.buyrecipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.chisom.buyrecipes.enums.CartLine.RECIPE;
import static com.chisom.buyrecipes.enums.OperationType.ADD;
import static com.chisom.buyrecipes.util.BuyRecipeUtil.wrap;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartService {

    private static final String ERROR_SAVING_RECIPE = "Could not process request at this time, Please try again or contact support";
    private final CartRepository cartRepository;
    private final RecipeConverter recipeConverter;
    private final ProductConverter productConverter;
    private final RecipeRepository recipeRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRecipeRepository cartRecipeRepository;

    //I am currently using pessimistic  locking here
    //Usually we would implement this better to using retries during lock failure and using a better approach to avoid deadlock situations, especially in extreme high concurrent situations
    @Transactional(timeout = 30)
    public void addRecipe(Long id, CartRecipeRequest request) {
        //find the cart
        Cart cart = findCartByIdWithLock(id);
        //find the recipe
        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> BuyRecipeException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Invalid Recipe")
                        .build());
        //Create and save the CartRecipe
        CartRecipe cartRecipe = new CartRecipe(cart, recipe);
        wrap(   // 1. action: save entity
                () -> cartRecipeRepository.save(cartRecipe),
                // 2. onError: send an alert or rollback some in-memory cache
                () -> logger.warn("Failed to save cartRecipe"),
                // 3. status
                HttpStatus.INTERNAL_SERVER_ERROR,
                // 4. client-facing message
                ERROR_SAVING_RECIPE
        );
        //Create and save the cart_items
        CartItem cartItem = new CartItem(
                cart, cartRecipe, RECIPE, request.quantity());
        wrap(   // 1. action: save entity
                () -> cartItemRepository.save(cartItem),
                // 2. onError: send an alert or rollback some in-memory cache
                () -> logger.warn("Failed to save cartItem"),
                // 3. status
                HttpStatus.INTERNAL_SERVER_ERROR,
                // 4. client-facing message
                ERROR_SAVING_RECIPE
        );

        //recalculate the cart total
        Cart recalculatedCart = recalculateCartTotal(cart, request.quantity(), cartRecipe, ADD);
        wrap(   // 1. action: save entity
                () -> cartRepository.save(recalculatedCart),
                // 2. onError: send an alert or rollback some in-memory cache
                () -> logger.warn("Failed to save recalculated Cart"),
                // 3. status
                HttpStatus.INTERNAL_SERVER_ERROR,
                // 4. client-facing message
                ERROR_SAVING_RECIPE
        );

    }

    @Transactional(readOnly = true)
    public CartResponse getCartItem(Long id) {

        Cart cart = findCartById(id);
        BigDecimal total = cart.getTotalIncents();

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new CartResponse(
                cart.getId(),
                total,
                items);
    }

    @Transactional
    public void deleteRecipeFromCart(Long cartId, Long cartRecipeId) {
        wrap(
                () -> cartRepository.hardDeleteRecipeAndRecalc(cartId, cartRecipeId),
                () -> logger.warn("Could not remove recipe {} from cart {}", cartRecipeId, cartId),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Could not delete recipe from cart at this time"
        );
    }

    private Cart findCartById(Long id) {
        return cartRepository.findCartsById(id).orElseThrow(() -> BuyRecipeException.builder()
                .message("Invalid cart id")
                .status(HttpStatus.BAD_REQUEST)
                .build());
    }

    private Cart findCartByIdWithLock(Long id) {
        return cartRepository.findByIdWithLock(id).orElseThrow(() -> BuyRecipeException.builder()
                .message("Invalid cart id")
                .status(HttpStatus.BAD_REQUEST)
                .build());
    }

    private Cart recalculateCartTotal(Cart cart, Long quantity,
                                      CartRecipe cartRecipe, OperationType operationType) {

        BigDecimal newTotal = cartRecipe.getRecipe().getIngredients()
                .stream()
                .map(ingredient -> ingredient.getProduct().getPriceIncents()
                        .multiply(new BigDecimal(quantity)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (operationType == ADD) {
            cart.addToTotalIncents(newTotal);
        } else {
            cart.subtractFromTotalIncents(newTotal);
        }

        return cart;
    }

    private CartItemResponse toItemResponse(CartItem item) {
        ProductResponse product = Optional.ofNullable(item.getProducts())
                .map(productConverter)
                .orElse(null);

        RecipeResponse recipe = Optional.ofNullable(item.getCartRecipe())
                .map(CartRecipe::getRecipe)
                .map(recipeConverter)
                .orElse(null);

        CartLine lineType = (product != null)
                ? CartLine.PRODUCT
                : RECIPE;

        return new CartItemResponse(
                item.getId(),
                item.getCartRecipe() != null ? item.getCartRecipe().getId() : null,
                lineType,
                item.getQuantity(),
                product,
                recipe
        );
    }
}
