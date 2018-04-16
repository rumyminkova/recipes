package com.teamtreehouse.recipes.web.controller;

import com.teamtreehouse.recipes.category.Category;
import com.teamtreehouse.recipes.category.CategoryService;
import com.teamtreehouse.recipes.ingredient.Ingredient;
import com.teamtreehouse.recipes.ingredient.IngredientService;
import com.teamtreehouse.recipes.prepstep.PrepStep;
import com.teamtreehouse.recipes.recipe.Recipe;
import com.teamtreehouse.recipes.recipe.RecipeService;
import com.teamtreehouse.recipes.user.User;
import com.teamtreehouse.recipes.user.UserService;
import com.teamtreehouse.recipes.web.FlashMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
public class RecipeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UserService userService;




    @ModelAttribute("categories")
    public Iterable<Category> populateCategories(){
        return this.categoryService.findAll();
    }



    @GetMapping("/")
    public String recipesIndexPage(){
       return "redirect:/recipes";

    }


    @RequestMapping("/recipes") //All recipes
    public String recipesList(Model model, Principal principal) {
        Iterable<Recipe> recipes = recipeService.findAll();
        Long selectedCategory=0L; //all categories
        model.addAttribute("selCat",selectedCategory);        //selected category
        model.addAttribute("recipesMap",recipeService.mapFavoriteRecipesForCurrentUser(recipes));
        return "recipe/index";
    }



    @RequestMapping("/recipes/category/{categoryId}")   //recipes by category
    public String recipesByCategory(@PathVariable Long categoryId, Model model) {
        List<Recipe> recipes = recipeService.findAllByCategory(categoryId);
        model.addAttribute("selCat",categoryId);//selected category
        model.addAttribute("recipesMap",recipeService.mapFavoriteRecipesForCurrentUser(recipes));
        return "recipe/index";
    }




    // Recipe details page
    @RequestMapping("recipes/{recipeId}/details")
    public String recipeDetails(@PathVariable Long recipeId, Model model) {
        Recipe recipe =recipeService.findOne(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("favorite", recipeService.isFavorite(recipe));
        return "recipe/detail";
    }


    @RequestMapping("recipes/add")     //form add
    public String formNewRecipe(Model model) {
        if(!model.containsAttribute("recipe")) {
            model.addAttribute("recipe",new Recipe());
        }
        model.addAttribute("action","/recipes");
        return "recipe/edit";
    }



    @RequestMapping(value = "/recipes",  params = {"save"}, method = RequestMethod.POST)
    public String addNewRecipe(@Valid Recipe recipe, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.recipe",result);
            redirectAttributes.addFlashAttribute("recipe",recipe);
            return "redirect:/recipes/add";
        }


        if(recipe.getIngredients().isEmpty()|| (recipe.getPrepSteps().isEmpty())) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Ingredients or preparation steps missing!", FlashMessage.Status.FAILURE));
            redirectAttributes.addFlashAttribute("recipe",recipe);
            return "redirect:/recipes/add";
        }

        ingredientService.save(recipe.getIngredients());
        recipeService.save(recipe);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Recipe added!", FlashMessage.Status.SUCCESS));
        return "redirect:/recipes";
    }



    // Form for editing

    @RequestMapping("recipes/{recipeId}/edit")  //form Edit
    public String formEditRecipe(@PathVariable Long recipeId, Model model,RedirectAttributes redirectAttributes) {
        Recipe recipe =recipeService.findOne(recipeId);

        if (!recipeService.isUserAdminOrOwner(recipe)){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only the owner or Admin are authorized to edit this recipe!", FlashMessage.Status.FAILURE));
            return String.format("redirect:/recipes");
        }

        if(!model.containsAttribute("recipe")) {
            model.addAttribute("recipe",recipe);
        }
        model.addAttribute("action",String.format("/recipes/%s",recipeId));
        return "recipe/edit";
    }


    // Update an existing recipe
    @RequestMapping(value = "/recipes/{recipeId}", params = {"save"}, method = RequestMethod.POST)
    public String updateRecipe(@Valid Recipe recipe, RedirectAttributes redirectAttributes, BindingResult result) {

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.recipe",result);
            redirectAttributes.addFlashAttribute("recipe",recipe);
            return String.format("redirect:/recipes/%s/edit",recipe.getId());
        }

        recipeService.save(recipe);

        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Recipe updated!", FlashMessage.Status.SUCCESS));

        return "redirect:/recipes";
    }


    @RequestMapping(value = {"/recipes","/recipes/{recipeId}"}, params={"addIngredient"})
    public String addIngredient(@Valid Recipe recipe, BindingResult result) {
        if(!result.hasErrors()) {
            recipe.getIngredients().add(new Ingredient());
        }
         return "recipe/edit";
    }




    @RequestMapping(value={"/recipes/{recipeId}","/recipes"}, params={"addStep"})
    public String addStep(@Valid Recipe recipe, BindingResult result) {
        if(!result.hasErrors()) {
            recipe.getPrepSteps().add(new PrepStep());
        }
        return "recipe/edit";
    }



    // Delete a recipe
    @RequestMapping(value = "/recipes/{recipeId}/delete", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable Long recipeId, RedirectAttributes redirectAttributes) {

        Recipe recipe =recipeService.findOne(recipeId);
        if (recipeService.isUserAdminOrOwner(recipe)) {
            recipeService.delete(recipe);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe deleted!", FlashMessage.Status.SUCCESS));
        }else{
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only the owner or Admin are authorized to delete this recipe!", FlashMessage.Status.FAILURE));
        }
        return "redirect:/recipes";
    }



     @RequestMapping("/recipes/favorites/{userId}") //All favorite recipes for user
    public String favRecipesListByUser(@PathVariable Long userId, Model model) {
        User user=userService.findOne(userId);
        List<Recipe> favRecipes=user.getFavRecipes();
        model.addAttribute("recipes",favRecipes);
        model.addAttribute("username",user.getUsername());
        return "user/profile";
    }


    @RequestMapping("/recipes/favorites") //All favorite recipes for the current user
    public String favRecipesList(Model model) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("recipes",recipeService.findFavoriteRecipesForCurrentUser());
        model.addAttribute("username",username);
        return "user/profile";
    }



    @RequestMapping("/recipes/{recipeId}/favorite") //toddle Favorite
    public String toddleFavRecipe(@PathVariable Long recipeId, Model model,Principal principal){
       recipeService.toddleFavoriteRecipe(recipeId);
       return  String.format("redirect:/recipes/%s/details",recipeId);
    }






    @RequestMapping("/recipes/search/ingredient/{ingredient}")   //recipes by description
    public String recipesByIngredient(@PathVariable String ingredient, Model model) {
        List<Recipe> recipes = recipeService.findByIngredient(ingredient);
        model.addAttribute("recipes",recipes);
        Long selectedCategory=0L;
        model.addAttribute("selCat",selectedCategory); //selected category
        model.addAttribute("recipesMap",recipeService.mapFavoriteRecipesForCurrentUser(recipes));
        return "recipe/index";
    }


    @RequestMapping("/recipes/search/description/{description}")   //recipes by description
    public String recipesByDescription(@PathVariable String description, Model model) {
        List<Recipe> recipes = recipeService.findByDescriptionIgnoreCaseContaining(description);
        model.addAttribute("recipes",recipes);
        Long selectedCategory=0L;
        model.addAttribute("selCat",selectedCategory); //selected category
        model.addAttribute("recipesMap",recipeService.mapFavoriteRecipesForCurrentUser(recipes));
        return "recipe/index";
    }






    @ExceptionHandler(ConstraintViolationException.class)
    public String recipeErrorPage(Model model, HttpServletRequest req, Exception ex) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(req);
        if(flashMap != null) {
            flashMap.put("flash",new FlashMessage("This recipe cannot be deleted because it was marked as favorite by a user", FlashMessage.Status.FAILURE));
        }
          //return redirect(req);
        return "redirect:" +req.getHeader("referer");
     }

}
