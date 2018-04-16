package com.teamtreehouse.recipes.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamtreehouse.recipes.core.BaseEntity;
import com.teamtreehouse.recipes.recipe.Recipe;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Rumy on 1/30/2018.
 */
@Entity
public class User extends BaseEntity implements UserDetails{

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String[] roles;

    @ManyToMany
    @JoinTable(name = "favRecipes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> favRecipes;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRoles());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User()
    {   super();
        favRecipes=new ArrayList<>();
    }

    public User(String username, String password, String... roles) {
        this();
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = (password);
    }


    public String[] getRoles() {
        return roles;
    }

   public void setRoles(String[] roles) {
        this.roles = roles;
    }

   public void setFavRecipes(List<Recipe> favRecipes) {
        this.favRecipes = favRecipes;
    }

    public List<Recipe> getFavRecipes() {
        return favRecipes;
    }

    public void addFavRecipe(Recipe recipe){
        favRecipes.add(recipe);
    }

}
