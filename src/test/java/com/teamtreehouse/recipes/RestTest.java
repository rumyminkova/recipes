package com.teamtreehouse.recipes;

import com.google.common.collect.Iterables;
import com.teamtreehouse.recipes.category.CategoryRepository;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeRepository;
import com.teamtreehouse.recipes.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Rumy on 4/15/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class RestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;




    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void whenGetRecipes_thenStatus200()throws Exception {


        mvc.perform(get("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"));
    }


    @Test
    @WithMockUser(username = "notAdmin", roles = {"USER"})
    public void recipe_cannotBeDeletedByNotAdminOrOwner()throws Exception {


        Recipe r=new Recipe.RecipeBuilder()
                .withName("TestRecipe")
                .withDescription("something")
                .withPrepTime(5)
                .withCookTime(10)
                .withCategory(categoryRepository.findByName("Dessert"))
                .build();
        r.setOwner(userRepository.findByUsername("UserAdmin"));
        recipeRepository.save(r);


        int count=Iterables.size(recipeRepository.findAll());



        mvc.perform(delete("/api/v1/recipes/"+r.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        assertEquals(count, Iterables.size(recipeRepository.findAll()));


    }


    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void recipe_canBeDeletedByAdmin()throws Exception {



        Recipe r=new Recipe.RecipeBuilder()
                .withName("TestRecipe1")
                .withDescription("something")
                .withPrepTime(5)
                .withCookTime(10)
                .withCategory(categoryRepository.findByName("Dessert"))
                .build();
        r.setOwner(userRepository.findByUsername("UserUser"));
        recipeRepository.save(r);

        int count=Iterables.size(recipeRepository.findAll());



        mvc.perform(delete("/api/v1/recipes/"+r.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertEquals( count-1,Iterables.size(recipeRepository.findAll()));

 }









}
