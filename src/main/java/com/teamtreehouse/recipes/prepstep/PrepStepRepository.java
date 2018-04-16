package com.teamtreehouse.recipes.prepstep;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rumy on 4/13/2018.
 */
@Repository
public interface PrepStepRepository extends CrudRepository<PrepStep,Long>{
}
