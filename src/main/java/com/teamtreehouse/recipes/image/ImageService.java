package com.teamtreehouse.recipes.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Rumy on 2/28/2018.
 */

public interface ImageService {

    Iterable<Image> findAll();
    Image findOne(Long id);
    Image save(Image image, MultipartFile file) throws IOException;
    Image update(Image image, MultipartFile file) throws IOException;
    void delete(Image image);
}
