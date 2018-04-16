package com.teamtreehouse.recipes.user;

/**
 * Created by Rumy on 3/4/2018.
 */
public class UserNameExistsException extends RuntimeException {
    public UserNameExistsException() {
        super("There is already an account registered with that username!");
    }
}
