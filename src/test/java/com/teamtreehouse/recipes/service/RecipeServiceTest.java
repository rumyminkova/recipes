package com.teamtreehouse.recipes.service;

import com.google.common.collect.Iterables;
import com.teamtreehouse.recipes.image.Image;
import com.teamtreehouse.recipes.image.ImageRepository;
import com.teamtreehouse.recipes.ingredient.Ingredient;
import com.teamtreehouse.recipes.ingredient.IngredientRepository;
import com.teamtreehouse.recipes.recipe.*;
import com.teamtreehouse.recipes.user.User;
import com.teamtreehouse.recipes.user.UserRepository;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Rumy on 3/6/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;


    @Mock
    private IngredientRepository ingredientRepository;


    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private RecipeService recipeService=new RecipeServiceImpl();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void findAll_shouldReturnTwo() throws Exception{
        List<Recipe> recipes = Arrays.asList(
                new Recipe.RecipeBuilder(1L).withName("Recipe1").withDescription("recipe1 descr").withPrepTime(10).withCookTime(30).build(),
                new Recipe.RecipeBuilder(2L).withName("Recipe2").withDescription("recipe2 descr").withPrepTime(30).withCookTime(30).build()
        );

        when(recipeRepository.findAll()).thenReturn(recipes);

        assertEquals("findAll should return two recipes",2, Iterables.size(recipeService.findAll()));

        verify(recipeRepository).findAll();

    }


    @Test
    public void findById_shouldReturnOne() throws Exception{
        when(recipeRepository.findOne(1L)).thenReturn(new Recipe());
        assertThat(recipeService.findOne(1L),instanceOf(Recipe.class));
        verify(recipeRepository).findOne(1L);

    }


    @Test
    public void findAllbyCategory_shouldReturnOne() throws Exception{
        Recipe recipe1= new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        Recipe recipe2=new Recipe.RecipeBuilder(2L).withName("Recipe2").build();

        List<Recipe> foundRecipe=new ArrayList<>();
        foundRecipe.add(recipe1);

        when(recipeRepository.findAllByCategory(10L)).thenReturn(foundRecipe);

        assertEquals("findAllByCategory should return one recipe",1, Iterables.size(recipeService.findAllByCategory(10L)));

        verify(recipeRepository).findAllByCategory(10L);

    }


    @Test(expected = RecipeDeleteException.class)
    public void recipeDelete_ThrowsException(){
        doThrow(RecipeDeleteException.class).when(recipeRepository).delete(any(Recipe.class));
        recipeService.delete(any(Recipe.class));
        verify(recipeService).delete(any(Recipe.class));
    }



    @Test
    public void recipeRemoveImage_shouldSetRecipeImageToNull() throws Exception{
        Recipe recipe1= new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        recipe1.setRecipeImage(new Image());
        when(recipeRepository.save(recipe1)).thenReturn(recipe1);

        recipeService.removeImage(recipe1);

        assertNull(recipe1.getRecipeImage());
    }


    @Test
    public void recipeAddImage_shouldSetRecipeImageToNotNull() throws Exception{
        Recipe recipe1= new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        Image image=new Image();
        when(recipeRepository.save(recipe1)).thenReturn(recipe1);

        recipeService.addImage(recipe1,image);

        assertNotNull(recipe1.getRecipeImage());
    }



    @Test
    public void findByIngredient_test() throws Exception{
        Ingredient ing=new Ingredient("Egg","fresh","1");

        Recipe recipe1= new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        recipe1.addIngredient(ing);
        Recipe recipe2= new Recipe.RecipeBuilder(2L).withName("Recipe2").build();
        recipe2.addIngredient(ing);

        Recipe recipe3= new Recipe.RecipeBuilder(3L).withName("Recipe3").build();

        List<Recipe> recipes=new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        List<Ingredient> ingredients=new ArrayList<>();
        ingredients.add(ing);
        when(ingredientRepository.findByItemIgnoreCaseContaining("egg")).thenReturn(ingredients);
        when(recipeService.findAll()).thenReturn(recipes);

        assertEquals("findByIngredient - egg should return 2",2, recipeService.findByIngredient("egg").size());
    }


    @Test
    public void savingNewRecipe_ShouldSetOwner()throws Exception {
        User testUser=new User("TestUser","password","ROLE_USER");
        testUser.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testUser, AuthorityUtils.createAuthorityList(testUser.getRoles())));

        Recipe testRecipe= new Recipe();

        doAnswer(
                invocation -> {
                    Recipe r = invocation.getArgumentAt(0, Recipe.class);
                    return r;
                }
        ).when(recipeRepository).save(any(Recipe.class));


        Recipe savedRecipe = recipeService.save(testRecipe);

        assertThat("The owner is set and it is testUser", savedRecipe.getOwner(),is(testUser));
        verify(recipeRepository).save(any(Recipe.class));
    }



    @Test
    public void mapFavoriteRecipesForCurrentUser_test() throws Exception {
        Recipe recipe1 = new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        Recipe recipe2 = new Recipe.RecipeBuilder(2L).withName("Recipe2").build();
        Recipe recipe3 = new Recipe.RecipeBuilder(3L).withName("Recipe3").build();
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        List<Recipe> favRecipes = new ArrayList<>();
        favRecipes.add(recipe1);
        favRecipes.add(recipe2);

        when(recipeService.findFavoriteRecipesForCurrentUser()).thenReturn(favRecipes);

        Map<Recipe, Boolean> favRecipesMap = recipeService.mapFavoriteRecipesForCurrentUser(recipes);

        Map<Recipe, Boolean> favRecipesMap1 = new HashMap<>();
        favRecipesMap1.put(recipe1, true);
        favRecipesMap1.put(recipe2, true);
        favRecipesMap1.put(recipe3, false);
        assertThat(favRecipesMap, is(favRecipesMap1));
    }



    @Test
    public void isFavorite_test() throws Exception{
        Recipe recipe1 = new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        Recipe recipe2 = new Recipe.RecipeBuilder(2L).withName("Recipe2").build();
        List<Recipe> favRecipes = new ArrayList<>();
        favRecipes.add(recipe1);
        favRecipes.add(recipe2);

        when(recipeService.findFavoriteRecipesForCurrentUser()).thenReturn(favRecipes);

        assertEquals(true,recipeService.isFavorite(recipe1));
  }



    @Test
    public void toddleFavoriteRecipe_addsRecipeToFavoritesIfNotFavorite() throws Exception{
        Recipe testRecipe=new Recipe();
        testRecipe.setId(10L);
        when(recipeRepository.findOne(1L)).thenReturn(testRecipe);

        User testUser=new User("TestUser","password","ROLE_ADMIN","ROLE_USER");
        testUser.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testUser, AuthorityUtils.createAuthorityList(testUser.getRoles())));

        when(userRepository.findByUsername("TestUser")).thenReturn(testUser);
        recipeService.toddleFavoriteRecipe(10L);

        MatcherAssert.assertThat("testUser has 1 favorite recipe",testUser.getFavRecipes(),hasSize(1));
    }


    @Test
    public void toddleFavoriteRecipe_removesRecipeFromFavoritesIfIsFavorite() throws Exception{
        Recipe testRecipe=new Recipe();
        testRecipe.setId(2L);
        when(recipeRepository.findOne(2L)).thenReturn(testRecipe);

        User testUser=new User("TestUser","password","ROLE_ADMIN","ROLE_USER");
        testUser.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testUser, AuthorityUtils.createAuthorityList(testUser.getRoles())));
        testUser.addFavRecipe(testRecipe);

        when(userRepository.findByUsername("TestUser")).thenReturn(testUser);

        recipeService.toddleFavoriteRecipe(2L);

        MatcherAssert.assertThat("testUser has 0 favorite recipe",testUser.getFavRecipes(),hasSize(0));
    }

}
