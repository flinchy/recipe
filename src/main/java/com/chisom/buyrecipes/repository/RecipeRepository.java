package com.chisom.buyrecipes.repository;

import com.chisom.buyrecipes.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @EntityGraph(attributePaths = {"ingredients", "ingredients.product"})
    @Query("select r from Recipe r")
    Page<Recipe> findAllRecipe(Pageable pageable);
}
