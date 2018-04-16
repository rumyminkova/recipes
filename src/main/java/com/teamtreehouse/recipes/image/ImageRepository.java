package com.teamtreehouse.recipes.image;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rumy on 2/28/2018.
 */
@Repository
public interface ImageRepository extends CrudRepository<Image,Long>{

}
