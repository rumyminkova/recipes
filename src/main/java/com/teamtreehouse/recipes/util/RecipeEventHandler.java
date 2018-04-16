package com.teamtreehouse.recipes.util;

import com.teamtreehouse.recipes.user.UserRepository;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by Rumy on 2/6/2018.
 */

@Component
@RepositoryEventHandler(Recipe.class)
public class RecipeEventHandler {
    private final UserRepository userRepository;

    @Autowired
    public RecipeEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @HandleBeforeSave
    public void setOwnerBaseOnLoggedInUser(Recipe recipe){
        String userName= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(userName);
        recipe.setOwner(user);
    }
}
