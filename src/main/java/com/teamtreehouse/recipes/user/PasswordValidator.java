package com.teamtreehouse.recipes.user;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Rumy on 2/12/2018.
 */
public class PasswordValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO u=(UserDTO) target;
        if(!(u.getPassword().equals(u.getMatchingPassword()))){
           errors.rejectValue("matchingPassword","pass.dont.match","Passwords don't match");
        };
    }
}
