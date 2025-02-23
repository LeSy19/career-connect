package com.person.careerconnect.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${careerconnect.upload-file.base-uri}")
    private String baseURI;

    public void createDirectory(String folder) throws URISyntaxException {

        URI uri = new URI(folder);
        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESS, PATH = " + tmpDir.toPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws IOException, URISyntaxException{
        //Tạo tên duy nhất
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        //Địa chỉ muốn lưu file
        URI uri = new URI(baseURI + folder + "/" + finalName);
        //chuyển qua dạng path
        Path path = Paths.get(uri);

        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, path,
                        StandardCopyOption.REPLACE_EXISTING);
        }

        return finalName;
    }
}
