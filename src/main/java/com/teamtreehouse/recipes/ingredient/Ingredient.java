package com.teamtreehouse.recipes.ingredient;

import com.teamtreehouse.recipes.core.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Rumy on 1/16/2018.
 */
@Entity
public class Ingredient extends BaseEntity {

    @NotNull
    @Size(min = 1, max = 20)
    private String item;

    @NotNull
    @Size(min = 1, max = 20)
    private String condition;

    @NotNull
    @Size(min = 1, max = 10)
    private String quantity;

    public Ingredient(){
        super();
    }


    public Ingredient(String item, String condition, String quantity) {
        this();
        this.item = item;
        this.condition = condition;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
