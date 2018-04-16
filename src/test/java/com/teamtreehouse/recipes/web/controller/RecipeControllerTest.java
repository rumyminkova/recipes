package com.teamtreehouse.recipes.web.controller;

import com.teamtreehouse.recipes.category.CategoryService;
import com.teamtreehouse.recipes.ingredient.Ingredient;
import com.teamtreehouse.recipes.ingredient.IngredientService;
import com.teamtreehouse.recipes.prepstep.PrepStep;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeService;
import com.teamtreehouse.recipes.user.User;
import com.teamtreehouse.recipes.user.UserService;
import com.teamtreehouse.recipes.web.FlashMessage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Rumy on 2/28/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerTest {

    private MockMvc mockMvc;


    @Mock
    private CategoryService categoryService;

    @Mock
    private RecipeService recipeService;


    @Mock
    private UserService userService;


    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private RecipeController controller;




   @Before
    public void setup(){

        User testUser=new User("TestUser","password","ROLE_ADMIN","ROLE_USER");
        testUser.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testUser, AuthorityUtils.createAuthorityList(testUser.getRoles())));

        testUser.setFavRecipes(createThreeRecipes());

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

    }


    private List<Recipe> createThreeRecipes(){
        Recipe recipe1=new Recipe.RecipeBuilder(1L).withName("Recipe1").build();
        Recipe recipe2=new Recipe.RecipeBuilder(2L).withName("Recipe2").build();
        Recipe recipe3=new Recipe.RecipeBuilder(3L).withName("Recipe3").build();

        List<Ingredient> allIngr= Arrays.asList(
                new Ingredient("Item1","Fresh","2"),
                new Ingredient("Item2","Jar","1")

        );

        List<PrepStep> allSteps= Arrays.asList(
                new PrepStep("Step1"),
                new PrepStep("Step2")

        );

        recipe3.setIngredients(allIngr);
        recipe3.setPrepSteps(allSteps);
        return Arrays.asList(recipe1,recipe2,recipe3);
    }








   @Test
    public void recipes_ShouldRenderIndexView() throws Exception{
       List<Recipe> recipes=createThreeRecipes();
       when(recipeService.findAll()).thenReturn(recipes);
       HashMap<Recipe,Boolean> recipeMap =new HashMap<>();
       recipeMap.put(recipes.get(0),true);
       recipeMap.put(recipes.get(1),false);
       recipeMap.put(recipes.get(2),true);

       when(recipeService.mapFavoriteRecipesForCurrentUser(recipes)).thenReturn(recipeMap);

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/index"))
                .andExpect(model().attributeExists("recipesMap"));
       Mockito.verify(recipeService).findAll();
       Mockito.verify(recipeService).mapFavoriteRecipesForCurrentUser(recipes);
    }



    @Test
    public void detail_ShouldRenderDetailView() throws Exception{
        Recipe recipe1=new Recipe.RecipeBuilder(1L).withName("Recipe1").build();


        when(recipeService.findOne(1L)).thenReturn(recipe1);
        when(recipeService.isFavorite(recipe1)).thenReturn(false);

        mockMvc.perform(get("/recipes/1/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/detail"))
                .andExpect(model().attributeExists("favorite"))
                .andExpect(model().attributeExists("recipe"))
        ;

        verify(recipeService).findOne(1L);
        verify(recipeService).isFavorite(any(Recipe.class));

    }


    @Test
    public void add_ShouldRedirectToView() throws Exception{
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/index"));
    }


    @Test
    public void addRecipe_getTest() throws Exception{
        mockMvc.perform(get("/recipes/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/edit"))
                .andExpect(model().attributeExists("recipe"));

    }


    @Test
    public void addRecipeWithNoIngredientsOrSteps_shouldRedirect() throws Exception{

        mockMvc.perform(post("/recipes")
                .param("name","New recipe")
                .param("description","something")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("save",""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/add"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.FAILURE)))
        );

    }


    @Test
    public void addRecipe_ShouldCreateNewRecipe() throws Exception{

        mockMvc.perform(post("/recipes")
                .param("name","New recipe")
                .param("description","something")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("ingredients[0].item", "Item1")
                .param("ingredients[0].condition", "fresh")
                .param("ingredients[0].quantity", String.valueOf(1))
                .param("prepSteps[0].description","step1")
                .param("save",""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.SUCCESS)))
                );

    }


    @Test
    public void favoriteRecipesForCurrentUser_shouldRedirectToProfile() throws Exception {

        mockMvc.perform(get("/recipes/favorites"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attributeExists("username"));

        verify(recipeService).findFavoriteRecipesForCurrentUser();

    }

    @Test
    public void favoriteRecipesForUser_shouldRedirectToProfile() throws Exception {
        User user = new User("User1", "password", "USER_USER");
        user.setFavRecipes(createThreeRecipes());
        when(userService.findOne(1L)).thenReturn(user);

        mockMvc.perform(get("/recipes/favorites/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attributeExists("username"));
    }



    @Test
    public void admin_shouldBeAbleToEditRecipe() throws Exception {
        Recipe testRecipe = new Recipe.RecipeBuilder(1L).withName("testRecipe").build();
        when(recipeService.findOne(1L)).thenReturn(testRecipe);
        Mockito.when(recipeService.isUserAdminOrOwner(testRecipe)).thenReturn(true);

        mockMvc.perform(get("/recipes/1/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/edit"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("action"));

    }



    @Test
    public void recipeByDescription_test() throws Exception {
        List<Recipe> recipes=createThreeRecipes();

        when(recipeService.findByDescriptionIgnoreCaseContaining("testString")).thenReturn(recipes);

        mockMvc.perform(get("/recipes/search/description/testString"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/index"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attributeExists("recipesMap"));

    }





    @Test
    public void recipeByIngredient_test() throws Exception {
        List<Recipe> recipes=createThreeRecipes();

        when(recipeService.findByIngredient("testString")).thenReturn(recipes);

        mockMvc.perform(get("/recipes/search/ingredient/testString"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/index"))
                .andExpect(model().attributeExists("recipes"))
                .andExpect(model().attributeExists("recipesMap"));

    }



    @Test
    public void recipeByCategory_test() throws Exception {
        List<Recipe> recipes=createThreeRecipes();

        when(recipeService.findAllByCategory(1L)).thenReturn(recipes);

        mockMvc.perform(get("/recipes/category/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/index"))
                .andExpect(model().attributeExists("selCat"))
                .andExpect(model().attributeExists("recipesMap"));

    }

    @Test
    public void newRecipeAddIngredient_test() throws Exception {
     mockMvc.perform(post("/recipes")
                .param("name","New recipe")
                .param("description","something")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("addIngredient",""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/edit"));

    }


    @Test
    public void newRecipeAddIngredient_invalidEntry() throws Exception {
        mockMvc.perform(post("/recipes")
                .param("name","New recipe")
                .param("description","something")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("ingredients[0].item", "")
                .param("ingredients[0].condition", "")
                .param("ingredients[0].quantity", "")
                .param("addIngredient",""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("recipe"))
                .andExpect(view().name("recipe/edit"));

    }



    @Test
    public void newRecipeAddStep_test() throws Exception {
        mockMvc.perform(post("/recipes")
                .param("name","New recipe")
                .param("description","something")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("addStep",""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/edit"));

    }

    @Test
    public void newRecipeAddStep_invalidEntry() throws Exception {
        mockMvc.perform(post("/recipes")
                .param("name","New recipe")
                .param("description","something")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("ingredients[0].item", "")
                .param("ingredients[0].condition", "")
                .param("ingredients[0].quantity", "")
                .param("addStep",""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("recipe"))
                .andExpect(view().name("recipe/edit"));

    }



    @Test
    public void admin_shouldBeAbleToDeleteRecipe() throws Exception {
        Recipe testRecipe = new Recipe.RecipeBuilder(1L).withName("testRecipe").build();
        when(recipeService.findOne(1L)).thenReturn(testRecipe);
        Mockito.when(recipeService.isUserAdminOrOwner(testRecipe)).thenReturn(true);

        mockMvc.perform(post("/recipes/1/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.SUCCESS)))
                );

    }


    @Test
    public void notOwnerOrAdmin_shouldNotBeAbleToDeleteRecipe() throws Exception {
        Recipe testRecipe = new Recipe.RecipeBuilder(1L).withName("testRecipe").build();
        when(recipeService.findOne(1L)).thenReturn(testRecipe);
        Mockito.when(recipeService.isUserAdminOrOwner(testRecipe)).thenReturn(false);

        mockMvc.perform(post("/recipes/1/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.FAILURE)))
                );

    }

    @Test
    public void editRecipe_test() throws Exception{

        Recipe testRecipe = new Recipe.RecipeBuilder(1L).withName("testRecipe").build();
        when(recipeService.findOne(1L)).thenReturn(testRecipe);

        mockMvc.perform(post("/recipes/1")
                .param("name","Edited recipe")
                .param("description","something new")
                .param("category",String.valueOf(1))
                .param("prepTime", String.valueOf(10))
                .param("cookTime", String.valueOf(60))
                .param("ingredients[0].item", "new Item1")
                .param("ingredients[0].condition", "fresh")
                .param("ingredients[0].quantity", String.valueOf(1))
                .param("prepSteps[0].description","step1")
                .param("prepSteps[1].description","step2")
                .param("save",""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.SUCCESS)))
                );

    }



    @Test
    public void addRecipe_ShouldFailWhenInvalidData() throws Exception{

        mockMvc.perform(post("/recipes")
                .param("name","")
                .param("description","")
                .param("category","")
                .param("prepTime","")
                .param("cookTime", "")
                .param("ingredients[0].item", "")
                .param("ingredients[0].condition", "")
                .param("ingredients[0].quantity", "")
                .param("prepSteps[0].description","")
                .param("save",""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/add"))
                .andExpect(flash().attribute("org.springframework.validation.BindingResult.recipe",hasProperty("fieldErrorCount", equalTo(8)))
                );

    }



    /*@Test
    public void toddleFavRecipe_test() throws Exception {
        Recipe testRecipe = new Recipe.RecipeBuilder(1L).withName("testRecipe").build();
        when(recipeService.findOne(1L)).thenReturn(testRecipe);

        User user=new User();
        user.setId(2L);
        user.addFavRecipe(testRecipe);

        Mockito.when(userService.toddleFavoriteRecipe(1,user).thenReturn(false);

        mockMvc.perform(post("/recipes/1/favorite"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.FAILURE)))
                );

    }*/



}
