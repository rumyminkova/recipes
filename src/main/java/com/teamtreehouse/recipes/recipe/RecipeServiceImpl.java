package com.teamtreehouse.recipes.recipe;

import com.teamtreehouse.recipes.image.Image;
import com.teamtreehouse.recipes.image.ImageRepository;
import com.teamtreehouse.recipes.ingredient.Ingredient;
import com.teamtreehouse.recipes.ingredient.IngredientRepository;
import com.teamtreehouse.recipes.user.User;
import com.teamtreehouse.recipes.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rumy on 3/12/2018.
 */
@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;


    @Autowired
    private IngredientRepository ingredientRepository;


    @Autowired
    private ImageRepository imageRepository;


    @Autowired
    private UserRepository userRepository;




    @Override
    public Iterable<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findOne(Long id) {
        return recipeRepository.findOne(id);
    }


    @Override
    public Recipe save(Recipe recipe) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.setOwner(user);
        return recipeRepository.save(recipe);
    }


    @Override
    public List<Recipe> findAllByCategory(Long id) {
        return recipeRepository.findAllByCategory(id);
    }


    @Override
    public void delete(Recipe recipe) {
        try {
            recipeRepository.delete(recipe);
        }catch (ConstraintViolationException ex) {
            throw ex;

        }
    }




    @Override
    public List<Recipe> findByIngredient(String ing) {
        List<Recipe> recipesByIngredient=new ArrayList<>();
        List<Ingredient> ingredientList=ingredientRepository.findByItemIgnoreCaseContaining(ing); //all Ingredients with the searched Item
        for (Recipe r:findAll()){
            for (Ingredient i: ingredientList) {
                if (r.getIngredients().contains(i)){
                    recipesByIngredient.add(r);
                }
            }
        }
        return recipesByIngredient;
    }


    @Override
    public List<Recipe> findByDescriptionIgnoreCaseContaining(String description) {
        return recipeRepository.findByDescriptionIgnoreCaseContaining(description);
    }


    @Override
    public Recipe removeImage(Recipe recipe) {
       Long imageId=recipe.getRecipeImage().getId();
       recipe.setRecipeImage(null);
       Recipe newRecipe =recipeRepository.save(recipe);
       imageRepository.delete(imageRepository.findOne(imageId));
       return newRecipe;
    }

    @Override
    public Recipe addImage(Recipe recipe, Image image) {
        recipe.setRecipeImage(image);
        return recipeRepository.save(recipe);
    }



    @Override
    public Map<Recipe, Boolean> mapFavoriteRecipesForCurrentUser(Iterable<Recipe> recipes) {
            Map<Recipe,Boolean> recipeMap=new HashMap<>();
            List<Recipe> favRecipes = findFavoriteRecipesForCurrentUser();
            for (Recipe r:recipes) {
                if (favRecipes.contains(r)){
                    recipeMap.put(r,true);
                }else recipeMap.put(r,false);
            }
            return recipeMap;
    }

    @Override
    public List<Recipe> findFavoriteRecipesForCurrentUser() {
        return recipeRepository.findFavoriteRecipesForCurrentUser();
    }


    @Override
    public boolean isFavorite(Recipe recipe) {
        List<Recipe> favrecipes=recipeRepository.findFavoriteRecipesForCurrentUser();
        return (favrecipes.contains(recipe));
    }

    @Override
    public boolean isUserAdminOrOwner(Recipe recipe){
        User currentUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean owner= recipe.getOwner().getUsername() == currentUser.getUsername();
        boolean role_admin = currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return (owner || role_admin);
    }


    public void toddleFavoriteRecipe(Long recipe_id){
        User currentUser=userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Recipe r= recipeRepository.findOne(recipe_id);

        List<Recipe> favs=currentUser.getFavRecipes();
        if(favs.contains(r)){
            favs.remove(r);
        }else {
            favs.add(r);
        }
        userRepository.save(currentUser);
    }

}
