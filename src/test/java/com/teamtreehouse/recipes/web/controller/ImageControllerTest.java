package com.teamtreehouse.recipes.web.controller;

import com.teamtreehouse.recipes.image.Image;
import com.teamtreehouse.recipes.image.ImageService;
import com.teamtreehouse.recipes.image.ImageValidator;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeService;
import com.teamtreehouse.recipes.web.FlashMessage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Rumy on 3/27/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

        private MockMvc mockMvc;

        @InitBinder("image")
        protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ImageValidator());
    }

        @InjectMocks
        private ImageController imageController;

        @Mock
        private ImageService imageService;


        @Mock
        private RecipeService recipeService;;

        @Before
        public void setup(){

            MockitoAnnotations.initMocks(this);
            mockMvc= MockMvcBuilders.standaloneSetup(imageController).build();
        }





         @Test
        public void addImageToRecipe_FailsIfEmptyFile() throws Exception {
            Image image=new Image();
            image.setFile(null);

            Recipe recipe =new Recipe();

            Mockito.when(recipeService.findOne(1L)).thenReturn(recipe);

            mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/images")
                     )
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/recipes/1/images/upload"))
                    .andExpect(flash().attribute("org.springframework.validation.BindingResult.image",hasProperty("fieldErrorCount", equalTo(1)))
                    );
        }

    @Test
    public void deleteImage_test() throws Exception {
        Recipe recipe = new Recipe();
        Image image = new Image();
        recipe.setRecipeImage(image);
        Mockito.when(recipeService.findOne(1L)).thenReturn(recipe);
        Mockito.when(recipeService.isUserAdminOrOwner(recipe)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/images/delete")
                 ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/1/details"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.SUCCESS))));

    }





    @Test
    public void uploadImage_test() throws Exception {
        Recipe recipe =new Recipe();
        Mockito.when(recipeService.findOne(1L)).thenReturn(recipe);
        Mockito.when(recipeService.isUserAdminOrOwner(recipe)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/images/upload")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("image/form"))
                .andExpect(model().attributeExists("image"))
                .andExpect(model().attribute("action","/recipes/1/images"))
                .andExpect(model().attribute("submit","Upload"));

    }


    @RequestMapping(value = "/recipes/{recipe_id}/images/{id}/edit")
    public String formEditImage(@PathVariable Long recipe_id, @PathVariable Long id, Model model) {
        if(!model.containsAttribute("image")) {
            model.addAttribute("image",imageService.findOne(id));
        }
        String s=String.format("/recipes/%s/images/%s",recipe_id,id);
        model.addAttribute("action",s);
        model.addAttribute("heading","Edit Image");
        model.addAttribute("submit","Update");

        return "image/form";
    }


    @Test
    public void editImage_test() throws Exception {
        Recipe recipe =new Recipe();
        Mockito.when(recipeService.findOne(1L)).thenReturn(recipe);

        Image image =new Image();
        Mockito.when(imageService.findOne(1L)).thenReturn(image);
        Mockito.when(recipeService.isUserAdminOrOwner(recipe)).thenReturn(true);


        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/images/1/edit")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("image/form"))
                .andExpect(model().attributeExists("image"))
                .andExpect(model().attribute("action","/recipes/1/images/1"))
                .andExpect(model().attribute("submit","Update"));

    }


    @Test
    public void uploadImage_ShouldFailIfNotAdminOrOwner() throws Exception {
        Recipe recipe =new Recipe();
        Mockito.when(recipeService.findOne(1L)).thenReturn(recipe);
        Mockito.when(recipeService.isUserAdminOrOwner(recipe)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/images/upload")
        )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/1/details"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.FAILURE))));

    }





  /* @Test
    public void addImageToRecipe_Test() throws Exception {

        Recipe recipe =new Recipe();
        Mockito.when(recipeService.findOne(1L)).thenReturn(recipe);

       MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg",
               "image/jpeg", "test image content".getBytes());

        Image image=new Image();
        image.setFile(multipartFile);


        Mockito.when(imageService.save(image,multipartFile)).thenReturn(image);


        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/images")
                 .param("image",image.)

                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/1/details"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.SUCCESS))));
    }*/






}
