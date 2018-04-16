package com.teamtreehouse.recipes.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Rumy on 3/12/2018.
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Iterable<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Image findOne(Long id) {
        return imageRepository.findOne(id);
    }

    @Override
    public Image save(Image image, MultipartFile file){
        try {
            image.setBytes(file.getBytes());

        } catch (IOException e) {

        }
         return imageRepository.save(image);

   }

    @Override
    public Image update(Image image, MultipartFile file){
        if(file != null && !file.isEmpty()) {
            try {
                image.setBytes(file.getBytes());
            } catch (IOException e) {


            }
        } else {
            Image oldImage = imageRepository.findOne(image.getId());
            image.setBytes(oldImage.getBytes());
        }

        return imageRepository.save(image);
    }

    @Override
    public void delete(Image image) {
        imageRepository.delete(image);

    }

}
