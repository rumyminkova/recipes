package com.teamtreehouse.recipes.recipe;

/**
 * Created by Rumy on 3/26/2018.
 */
public class RecipeDeleteException extends RuntimeException {
    public RecipeDeleteException() {super("This recipe cannot be deleted!");
    }
}
