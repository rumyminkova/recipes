package com.teamtreehouse.recipes.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rumy on 2/23/2018.
 */

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public void save(Ingredient ingredient) {
      ingredientRepository.save(ingredient);
    }

    @Override
    public Iterable<Ingredient> save(Iterable<Ingredient> ingredients) {
        return ingredientRepository.save(ingredients);
    }


    @Override
    public List<Ingredient> findByItemIgnoreCaseContaining(String item) {
        return ingredientRepository.findByItemIgnoreCaseContaining(item);
    }
}
