package com.teamtreehouse.recipes.ingredient;


import java.util.List;

/**
 * Created by Rumy on 2/18/2018.
 */
public interface IngredientService {
    void save(Ingredient ingredient);
    Iterable<Ingredient> save(Iterable<Ingredient> ingredients);

    List<Ingredient> findByItemIgnoreCaseContaining(String item);
}
