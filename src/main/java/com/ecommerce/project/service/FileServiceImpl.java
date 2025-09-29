package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        //prendo il nome del file
        String originalFileName = image.getOriginalFilename();

        //genero un id univoco
        String randomImgId = UUID.randomUUID().toString();

        //img.jpg --> 1234 --> 1234.jpg
        assert originalFileName != null;
        String fileName = randomImgId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
