package com.teamtreehouse.recipes.web.controller;

import com.teamtreehouse.recipes.user.User;
import com.teamtreehouse.recipes.user.UserDTO;
import com.teamtreehouse.recipes.user.UserNameExistsException;
import com.teamtreehouse.recipes.user.UserService;
import com.teamtreehouse.recipes.web.FlashMessage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Rumy on 3/5/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void setup(){
        mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }


    private User userBuilder() {
        User user=new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u");
        user.setRoles(new String[]{"ROLE_USER"});
        return user;
    }

    private UserDTO userDTOBuilder() {
        UserDTO userDTO=new UserDTO();
        userDTO.setUsername("TestUser");
        userDTO.setPassword("password");
        userDTO.setMatchingPassword("password");
        return userDTO;
    }




    @Test
    public void validPassword_ShouldBeLessThan16Characters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .accept(MediaType.TEXT_HTML)
                .param("username","TestUser")
                .param("password", "12345678901234567890")
                .param("matchingPassword", "12345678901234567890"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("org.springframework.validation.BindingResult.userDTO",hasProperty("fieldErrorCount", equalTo(1))))
                .andExpect(redirectedUrl("/signup")
                );
    }

    @Test
    public void validPassword_ShouldBeMoreThan7Characters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .accept(MediaType.TEXT_HTML)
                .param("username","TestUser")
                .param("password", "1234567")
                .param("matchingPassword", "1234567"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("org.springframework.validation.BindingResult.userDTO",hasProperty("fieldErrorCount", equalTo(1))))
                .andExpect(redirectedUrl("/signup")
                );
    }


    @Test
    public void passwordsShouldMatch_whenRegisteringNewUser()
            throws Exception {
        mockMvc.perform(post("/signup")
                .accept(MediaType.TEXT_HTML)
                .param("username","SomeUser")
                .param("password", "12345678")
                .param("matchingPassword", "1234pass"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("org.springframework.validation.BindingResult.userDTO",hasProperty("fieldErrorCount", equalTo(1))))
                .andExpect(redirectedUrl("/signup")
                );
    }


    @Test
    public void test_RegisteringNewUser()
            throws Exception {
        mockMvc.perform(post("/signup")
                .accept(MediaType.TEXT_HTML)
                .param("username","SomeUser")
                .param("password", "12345678")
                .param("matchingPassword", "12345678"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("flash", Matchers.hasProperty("status", Matchers.equalTo(FlashMessage.Status.SUCCESS)))
                );
    }


    @Test
    public void login_rendersLoginPage()throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"))
                .andExpect(model().attributeDoesNotExist("flash"))
                .andExpect(model().attributeExists("user")
                );

    }




    @Test
    public void postWithUsernameThatAlreadyExists_ShouldRedirectBack()throws Exception {
        when(userService.registerNewUser(any())).thenThrow(UserNameExistsException.class);

        mockMvc.perform(post("/signup")
                .param("username","TestUser")
                .param("password", "password")
                .param("matchingPassword", "password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("flash",hasProperty("status", equalTo( FlashMessage.Status.FAILURE))));


         verify(userService).registerNewUser(any());
    }



    @Test
    public void postSignUp_ShouldCreateNewUser()throws Exception {

        when(userService.registerNewUser(any())).thenReturn(any(User.class));

        mockMvc.perform(post("/signup")
                .param("username", "TestUser")
                .param("password", "password")
                .param("matchingPassword", "password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("flash", hasProperty("status", equalTo(FlashMessage.Status.SUCCESS))));


        verify(userService).registerNewUser(any());
    }


    @Test
    public void signup_rendersSignUpPage()throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/user/signup"))
                .andExpect(model().attributeExists("userDTO"));

    }


}
