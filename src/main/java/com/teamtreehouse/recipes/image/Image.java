package com.teamtreehouse.recipes.image;

import com.teamtreehouse.recipes.core.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * Created by Rumy on 2/28/2018.
 */
@Entity
public class Image extends BaseEntity{

    @Lob
    private byte[] bytes;


    private String hash;


    @Transient
    private MultipartFile file;



    public Image(){
        super();
    }


    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
