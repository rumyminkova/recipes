package com.teamtreehouse.recipes.category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rumy on 3/12/2018.
 */
@Repository
public interface  CategoryRepository extends CrudRepository<Category,Long>{
    Category findByName(String name);
}
