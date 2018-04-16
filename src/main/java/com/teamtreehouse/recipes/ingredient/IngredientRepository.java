package com.teamtreehouse.recipes.ingredient;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rumy on 2/18/2018.
 */
@Repository
public interface IngredientRepository extends CrudRepository<Ingredient,Long>{

    List<Ingredient> findByItemIgnoreCaseContaining(String item);
}
