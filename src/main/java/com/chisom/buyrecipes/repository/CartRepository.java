package com.chisom.buyrecipes.repository;

import com.chisom.buyrecipes.entity.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {
            "cartItems",
            "cartItems.products",
            "cartItems.cartRecipe.recipe"
    })
    Optional<Cart> findCartsById(Long id); //@EntityGraph to avoid N+1 problem of hibernate, fetch everything in one query

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cart c where c.id = :id")
    Optional<Cart> findByIdWithLock(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = """
            WITH recipe_meta AS (
              -- how many of that recipe are in the cart
              SELECT cr.recipe_id,
                     ci.quantity AS serving
                FROM cart_recipes cr
                JOIN cart_items ci
                  ON ci.cart_recipe_id = cr.id
               WHERE cr.id = :cartRecipeId
            ),
            removed_items AS (
              -- list each ingredient & how many we’re removing
              SELECT ri.product_id,
                     (ri.quantity * rm.serving) AS quantity
                FROM recipe_ingredient ri
                JOIN recipe_meta rm
                  ON rm.recipe_id = ri.recipe_id
            ),
            del_items AS (
              -- hard‐delete the cart_items
              DELETE FROM cart_items
              WHERE cart_recipe_id = :cartRecipeId
            ),
            del_recipe AS (
              -- hard‐delete the marker
              DELETE FROM cart_recipes
              WHERE id = :cartRecipeId
            ),
            delta AS (
              -- compute the money value of what was just removed
              SELECT COALESCE(SUM(p.price_in_cents * ri.quantity), 0) AS amt
                FROM removed_items ri
                JOIN products p
                  ON p.id = ri.product_id
            )
            -- subtract from the cart’s total
            UPDATE carts
               SET total_in_cents = total_in_cents - (SELECT amt FROM delta)
             WHERE id = :cartId;
            """,
            nativeQuery = true)
    void hardDeleteRecipeAndRecalc(
            @Param("cartId") Long cartId,
            @Param("cartRecipeId") Long cartRecipeId
    );

    @Modifying
    @Transactional
    @Query(
            value = """
                    WITH recipe_meta AS (
                            -- find out how many quantity of that recipe are in this cart
                            SELECT cr.recipe_id,
                                   ci.quantity   AS serving
                              FROM cart_recipes cr
                              JOIN cart_items ci
                                ON ci.cart_recipe_id = cr.id
                             WHERE cr.id = :cartRecipeId
                          ),
                        removed_items AS (
                                -- for each ingredient, compute how many product we’re removing
                                SELECT ri.product_id, rm.serving AS quantity
                                  FROM recipe_ingredient ri
                                  JOIN recipe_meta rm
                                    ON rm.recipe_id = ri.recipe_id
                              ),
                         -- now soft-delete the rows
                              del_items   AS (
                                UPDATE cart_items
                                   SET deleted_at = CURRENT_TIMESTAMP
                                 WHERE cart_recipe_id = :cartRecipeId
                              ),
                        del_recipe  AS (
                                UPDATE cart_recipes
                                   SET deleted_at = CURRENT_TIMESTAMP
                                 WHERE id = :cartRecipeId
                              ),
                        -- compute the total money value of those removed ingredients
                             delta AS (
                               SELECT COALESCE(SUM(p.price_in_cents * ri.quantity), 0) AS amt
                                 FROM removed_items ri
                                 JOIN products p
                                   ON p.id = ri.product_id
                             )
                         -- subtract it from the cart’s total
                              UPDATE carts
                                 SET total_in_cents = total_in_cents - (SELECT amt FROM delta)
                               WHERE id = :cartId;
                    """,
            nativeQuery = true
    )
    void softDeleteRecipeAndRecalc(
            @Param("cartId") Long cartId,
            @Param("cartRecipeId") Long cartRecipeId
    );
}
