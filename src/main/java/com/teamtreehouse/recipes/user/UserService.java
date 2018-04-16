package com.teamtreehouse.recipes.user;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Rumy on 2/2/2018.
 */
public interface UserService extends UserDetailsService{
        User findByUsername(String username);

        User findOne(Long id);
        User registerNewUser(UserDTO userDTO) throws UserNameExistsException;
        User save (User user);
        Iterable<User> findAll();



}
