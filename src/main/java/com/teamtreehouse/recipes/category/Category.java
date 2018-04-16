package com.teamtreehouse.recipes.category;


import com.teamtreehouse.recipes.core.BaseEntity;

import javax.persistence.Entity;

/**
 * Created by Rumy on 1/16/2018.
 */
@Entity
public class Category extends BaseEntity {
    private String name;

    protected Category(){
        super();
    }

    public Category(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
