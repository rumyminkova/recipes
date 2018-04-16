package com.teamtreehouse.recipes.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Rumy on 3/13/2018.
 */
 @Component
 public class ImageValidator implements Validator {
        @Autowired
        private Validator validator;

        @Override
        public boolean supports(Class<?> clazz) {
            return Image.class.equals(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            Image image = (Image)target;


            if(image.getId() == null && (image.getFile() == null || image.getFile().isEmpty())) {
                errors.rejectValue("file","file.required","Please choose a file to upload");
            }

        }
 }
