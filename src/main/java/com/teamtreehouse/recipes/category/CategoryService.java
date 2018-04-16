package com.teamtreehouse.recipes.category;

/**
 * Created by Rumy on 1/16/2018.
 */
public interface CategoryService {
        Iterable<Category> findAll();
        Category findOne(Long id);
        Category findByName(String name);
}
