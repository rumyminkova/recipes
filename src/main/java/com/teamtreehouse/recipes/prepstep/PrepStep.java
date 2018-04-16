package com.teamtreehouse.recipes.prepstep;

import com.teamtreehouse.recipes.core.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Rumy on 1/16/2018.
 */
@Entity
public class PrepStep extends BaseEntity {

    @NotNull
    @Size(min = 1, max = 50)
    private String description;

    public PrepStep(){
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public PrepStep(String description) {
        this.description = description;
    }
}
