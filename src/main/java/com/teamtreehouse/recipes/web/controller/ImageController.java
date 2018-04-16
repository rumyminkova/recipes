package com.teamtreehouse.recipes.web.controller;

import com.teamtreehouse.recipes.image.Image;
import com.teamtreehouse.recipes.image.ImageService;
import com.teamtreehouse.recipes.image.ImageValidator;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeService;
import com.teamtreehouse.recipes.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Rumy on 2/28/2018.
 */
@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;


    @Autowired
    private RecipeService recipeService;

    @InitBinder("image")
    protected void initBinder(WebDataBinder binder) {
          binder.setValidator(new ImageValidator());
    }



    // image data
    @RequestMapping("/recipes/images/{id}.jpg")
    @ResponseBody
    public byte[] recipeImage(@PathVariable Long id) {
        return imageService.findOne(id).getBytes();
    }



    // Upload a new Image

    @RequestMapping(value = "/recipes/{recipe_id}/images", method = RequestMethod.POST)
    public String addImage(@PathVariable Long recipe_id, @Valid Image image, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.image",result);
            redirectAttributes.addFlashAttribute("image",image);
            return String.format("redirect:/recipes/%s/images/upload",recipe_id);
        }

        Image savedImage = imageService.save(image, image.getFile());

        Recipe recipe=recipeService.findOne(recipe_id);
        recipeService.addImage(recipe,savedImage);

        // Add flash message for success
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Image successfully uploaded!", FlashMessage.Status.SUCCESS));

        return String.format("redirect:/recipes/%s/details",recipe_id);
    }

    // Form for uploading a new Image
    @RequestMapping("/recipes/{recipe_id}/images/upload")
    public String formNewImage(@PathVariable Long recipe_id,Model model,RedirectAttributes redirectAttributes) {
        Recipe recipe=recipeService.findOne(recipe_id);

        if (!recipeService.isUserAdminOrOwner(recipe)){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only the owner or Admin are authorized to upload image for this recipe!", FlashMessage.Status.FAILURE));
            return String.format("redirect:/recipes/%s/details",recipe_id);
        }

        if(!model.containsAttribute("image")) {
            model.addAttribute("image",new Image());
        }
        String s=String.format("/recipes/%s/images",recipe_id);
        model.addAttribute("action",s);
        model.addAttribute("submit","Upload");

        return "image/form";
    }


    // Update an existing Image
    @RequestMapping(value = "/recipes/{recipe_id}/images/{id}", method = RequestMethod.POST)
    public String updateImage(@PathVariable Long recipe_id,@Valid Image image, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.image",result);
            redirectAttributes.addFlashAttribute("image",image);
            return String.format("redirect:/recipes/images/%s/edit",image.getId());
        }
        imageService.update(image,image.getFile());
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Image updated!", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/recipes/%s/details",recipe_id);
    }



    // Form for editing an existing Image
    @RequestMapping(value = "/recipes/{recipe_id}/images/{id}/edit")
    public String formEditImage(@PathVariable Long recipe_id,@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Recipe recipe=recipeService.findOne(recipe_id);

        if (!recipeService.isUserAdminOrOwner(recipe)){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only the owner or Admin are authorized to upload image for this recipe!", FlashMessage.Status.FAILURE));
            return String.format("redirect:/recipes/%s/details",recipe_id);
        }

        if(!model.containsAttribute("image")) {
            model.addAttribute("image",imageService.findOne(id));
        }
        String s=String.format("/recipes/%s/images/%s",recipe_id,id);
        model.addAttribute("action",s);
        model.addAttribute("heading","Edit Image");
        model.addAttribute("submit","Update");

        return "image/form";
    }


    @RequestMapping(value = "/recipes/{recipe_id}/images/delete", method = RequestMethod.POST)
    public String removeImage(@PathVariable Long recipe_id, RedirectAttributes redirectAttributes) {
        Recipe recipe=recipeService.findOne(recipe_id);

        if (!recipeService.isUserAdminOrOwner(recipe)){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only the owner or Admin are authorized to upload image for this recipe!", FlashMessage.Status.FAILURE));
            return String.format("redirect:/recipes/%s/details",recipe_id);
        }

        recipeService.removeImage(recipe);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Image removed!", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/recipes/%s/details",recipe_id);
    }



}
