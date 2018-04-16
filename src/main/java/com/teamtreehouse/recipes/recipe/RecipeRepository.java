package com.teamtreehouse.recipes.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rumy on 1/16/2018.
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe,Long>{
    @Query("select r from Recipe r where r.category.id=:id")
    List<Recipe> findAllByCategory(@Param("id") Long id);

    List<Recipe> findByDescriptionIgnoreCaseContaining(String description);

    @Query(nativeQuery = true, value ="SELECT * FROM recipe WHERE id IN (SELECT recipe_id FROM favrecipes WHERE user_id =:#{principal.id})")
    List<Recipe> findFavoriteRecipesForCurrentUser();


    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or #recipe.owner.username==authentication.name")
    void delete(@Param("recipe") Recipe recipe);
}
