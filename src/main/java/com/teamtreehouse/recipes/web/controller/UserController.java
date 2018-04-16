package com.teamtreehouse.recipes.web.controller;

import com.teamtreehouse.recipes.user.*;
import com.teamtreehouse.recipes.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Rumy on 2/7/2018.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "user/login";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(WebRequest request, Model model) {
        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", new UserDTO());
        }
        return "/user/signup";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String newUser(@Valid UserDTO userDTO,BindingResult result, RedirectAttributes redirectAttributes){

        PasswordValidator passValidator = new PasswordValidator();
        passValidator.validate(userDTO, result);

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDTO",result);
            redirectAttributes.addFlashAttribute("userDTO",userDTO);
            return "redirect:/signup"; }

        //Check if username doesn’t already exist and save
         userService.registerNewUser(userDTO);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("User successfully registered!", FlashMessage.Status.SUCCESS));

        return "redirect:/login";
    }



    @ExceptionHandler(UserNameExistsException.class)
    public String databaseError(Model model, HttpServletRequest req, Exception ex) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(req);
        if(flashMap != null) {
            flashMap.put("flash",new FlashMessage(ex.getMessage(), FlashMessage.Status.FAILURE));
        }
       // return redirect(req);
        return "redirect:" +req.getHeader("referer");
    }

}
