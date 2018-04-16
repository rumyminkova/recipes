package com.teamtreehouse.recipes.service;

import com.teamtreehouse.recipes.recipe.RecipeRepository;
import com.teamtreehouse.recipes.user.*;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rumy on 3/20/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private UserService service=new UserServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }





    @Test
    public void findOne_ShouldReturnOne() throws Exception{
        when(repository.findOne(1L)).thenReturn(new User());
        assertThat(service.findOne(1L),org.hamcrest.Matchers.instanceOf(User.class));
        verify(repository).findOne(1L);
    }

    @Test(expected = UserNameExistsException.class)
    public void registerNewUser_ThrowsUserNameExistsException(){
        UserDTO userDTO=new UserDTO("UserTest","password1","password1");
        when(service.findByUsername("UserTest")).thenReturn(new User());
        when(service.registerNewUser(userDTO)).thenThrow(UserNameExistsException.class);

        service.registerNewUser(userDTO);

        verify(service).registerNewUser(userDTO);
    }


    @Test
    public void registerNewUser_createsNewUser() throws Exception{

        UserDTO userDTO=new UserDTO("UserTest","password1","password1");
        when(service.registerNewUser(userDTO)).thenReturn(new User());
        when(service.save(any(User.class))).thenReturn(new User());

        MatcherAssert.assertThat(service.registerNewUser(userDTO),org.hamcrest.Matchers.instanceOf(User.class));
    }



    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_throwsUsernameNotFoundException() throws Exception{
        when(service.loadUserByUsername("TestName")).thenThrow(UsernameNotFoundException.class);
        service.loadUserByUsername("TestName");
        verify(service.loadUserByUsername("TestName"));
    }


    @Test
    public void loadUserByUsername_ShouldReturnUser() throws Exception {
        User testUser = new User();
        when(service.findByUsername("TestName")).thenReturn(testUser);

        User user = (User) service.loadUserByUsername("TestName");

        assertThat(user, is(testUser));
    }


}
