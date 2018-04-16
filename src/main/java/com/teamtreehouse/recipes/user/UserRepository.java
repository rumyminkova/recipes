package com.teamtreehouse.recipes.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Rumy on 2/1/2018.
 */
@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);

}

