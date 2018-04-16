package com.teamtreehouse.recipes.core;

import com.teamtreehouse.recipes.category.Category;
import com.teamtreehouse.recipes.category.CategoryRepository;
import com.teamtreehouse.recipes.ingredient.Ingredient;
import com.teamtreehouse.recipes.prepstep.PrepStep;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeRepository;
import com.teamtreehouse.recipes.user.User;
import com.teamtreehouse.recipes.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Rumy on 1/15/2018.
 */
@Component
public class DatabaseLoader implements ApplicationRunner{
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;


    @Autowired
    public DatabaseLoader(CategoryRepository categoryRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository=userRepository;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //create Users with password: password
         userRepository.save(new User("UserAdmin","$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u","ROLE_ADMIN","ROLE_USER"));
         userRepository.save(new User("UserUser","$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u","ROLE_USER"));


         //create categories

        categoryRepository.save(new Category("Dessert"));
        categoryRepository.save(new Category("Lunch"));
        categoryRepository.save(new Category("Dinner"));
        categoryRepository.save(new Category("Breakfast"));



        Recipe r=new Recipe.RecipeBuilder(10L)
                .withName("Brioche French Toast with Nutella")
                .withDescription("Yummmy dessert toast covered in egg wash and topped with nutella and seasonal fruit.")
                .withPrepTime(5)
                .withCookTime(10)
                .withCategory(categoryRepository.findByName("Dessert"))
                .build();


        List<Ingredient> allIngr= Arrays.asList(
                new Ingredient("Eggs","Fresh","2"),
                new Ingredient("Nutella","Jar","1"),
                new Ingredient("Bread","Fresh","2")

        );

        List<PrepStep> allSteps= Arrays.asList(
                new PrepStep("Crack your eggs and beat them."),
                new PrepStep("Toast the bread."),
                new PrepStep(" Cover bread with eggs. Fry for 15 minutes.")

        );

        r.setIngredients(allIngr);
        r.setPrepSteps(allSteps);

        r.setOwner(userRepository.findByUsername("UserAdmin"));

        recipeRepository.save(r);
    }
}














