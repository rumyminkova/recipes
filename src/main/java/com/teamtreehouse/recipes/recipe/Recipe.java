package com.teamtreehouse.recipes.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamtreehouse.recipes.category.Category;
import com.teamtreehouse.recipes.core.BaseEntity;
import com.teamtreehouse.recipes.image.Image;
import com.teamtreehouse.recipes.ingredient.Ingredient;
import com.teamtreehouse.recipes.prepstep.PrepStep;
import com.teamtreehouse.recipes.user.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rumy on 1/16/2018.
 */
@Entity
public class Recipe extends BaseEntity {

    @Size(min = 1, max = 50)
    private String name;

    @Size(min = 1, max = 100)
    private String description;


    @NotNull
    @Min(value = 1,message = "Must be number 1 or grater")
    private int prepTime;

    @NotNull
    @Min(value=1,message = "Must be number 1 or grater")
    private int cookTime;


    @OneToMany(cascade = CascadeType.ALL)
    private List<@Valid Ingredient> ingredients= new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    private List<@Valid PrepStep> prepSteps = new ArrayList<>();

    @ManyToOne
    private Category category;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image recipeImage;






    public Recipe() {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<PrepStep> getPrepSteps() {
        return prepSteps;
    }

    public void setPrepSteps(List<PrepStep> prepSteps) {
        this.prepSteps = prepSteps;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void addPrepStep(PrepStep prepStep) {
        prepSteps.add(prepStep);
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Image getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Image recipeImage) {
        this.recipeImage = recipeImage;
    }




    public static class RecipeBuilder {

        private Long id;
        private String name;
        private String description;
        private int prepTime;
        private int cookTime;
        private Category category;

        public RecipeBuilder(){}


        public RecipeBuilder(Long id) {
            this.id = id;
        }


        public RecipeBuilder withId(Long id) {
            this.id = id;
            return this;
        }



        public RecipeBuilder withName(String name) {
            this.name=name;
            return this;
        }

        public RecipeBuilder withDescription(String description) {
            this.description=description;
            return this;
        }


        public RecipeBuilder withPrepTime(int prepTime) {
            this.prepTime=prepTime;
            return this;
        }

        public RecipeBuilder withCookTime(int cookTime) {
            this.cookTime=cookTime;
            return this;
        }

        public RecipeBuilder withCategory(Category category) {
            this.category=category;
            return this;
        }



        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.setId(id);
            recipe.setName(name);
            recipe.setDescription(description);
            recipe.setPrepTime(prepTime);
            recipe.setCookTime(cookTime);
            recipe.setCategory(category);
            return recipe;
        }
    }



}
