package com.teamtreehouse.recipes.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Rumy on 1/16/2018.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findOne(Long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
