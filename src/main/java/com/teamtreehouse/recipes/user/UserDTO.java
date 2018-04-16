package com.teamtreehouse.recipes.user;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Rumy on 2/5/2018.
 */

public class UserDTO {

        @Column(nullable = false,unique = true)
        @Size(min = 8, max = 20, message = "The username must be {min} to {max} characters in length.")
        private String username;

        @Pattern(regexp="[0-9a-zA-Z]{8,15}",message = "The password must be between 8 and 15 characters long, only letters and numbers")
        private String password;
        private String matchingPassword;

    public UserDTO() {
    }

    public UserDTO(String username, String password,String matchingPassword) {
        this.username = username;
        this.password = password;
        this.matchingPassword=matchingPassword;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
