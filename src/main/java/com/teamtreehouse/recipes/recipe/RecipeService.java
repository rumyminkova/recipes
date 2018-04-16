package com.teamtreehouse.recipes.recipe;

import com.teamtreehouse.recipes.image.Image;

import java.util.List;
import java.util.Map;

/**
 * Created by Rumy on 3/12/2018.
 */
public interface RecipeService {

        Iterable<Recipe> findAll();

        Recipe findOne(Long id);

        Recipe save(Recipe recipe);

        void delete(Recipe recipe) throws org.hibernate.exception.ConstraintViolationException;

        List<Recipe> findAllByCategory(Long id);

        List<Recipe> findByIngredient(String ing);

        List<Recipe> findByDescriptionIgnoreCaseContaining(String description);


        Recipe removeImage(Recipe recipe);

        Recipe addImage(Recipe recipe, Image image);


        List<Recipe> findFavoriteRecipesForCurrentUser();

        Map<Recipe, Boolean> mapFavoriteRecipesForCurrentUser(Iterable<Recipe> recipes);

        boolean isFavorite(Recipe recipe);

        boolean isUserAdminOrOwner(Recipe recipe);

        void toddleFavoriteRecipe(Long recipe_id);


}
