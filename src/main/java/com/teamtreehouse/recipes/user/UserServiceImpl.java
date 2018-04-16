package com.teamtreehouse.recipes.user;

import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Rumy on 2/2/2018.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private RecipeRepository recipeRepository;


    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load the user from the DB

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found!");
        }

        return user;
    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }


    @Transactional
    @Override
    public User registerNewUser(UserDTO userDTO){
        User foundUser=userRepository.findByUsername(userDTO.getUsername());

            if (foundUser!=null){
                 throw new UserNameExistsException();
            }else {
                User user = new User(userDTO.getUsername(), bCryptPasswordEncoder.encode(userDTO.getPassword()), "ROLE_USER");
                return userRepository.save(user);
            }
    }

}
