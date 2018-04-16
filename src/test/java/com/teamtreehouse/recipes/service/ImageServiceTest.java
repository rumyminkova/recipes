package com.teamtreehouse.recipes.service;

import com.teamtreehouse.recipes.image.Image;
import com.teamtreehouse.recipes.image.ImageRepository;
import com.teamtreehouse.recipes.image.ImageService;
import com.teamtreehouse.recipes.image.ImageServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rumy on 3/27/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

        @Mock
        private ImageRepository imageRepository;


        @InjectMocks
        private ImageService service=new ImageServiceImpl();

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);
        }





        @Test
        public void findOne_ShouldReturnOne() throws Exception{
            when(imageRepository.findOne(1L)).thenReturn(new Image());
            assertThat(service.findOne(1L),org.hamcrest.Matchers.instanceOf(Image.class));
            verify(imageRepository).findOne(1L);
        }





}
