package com.person.careerconnect.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.person.careerconnect.domain.response.file.ResUploadFileDTO;
import com.person.careerconnect.service.FileService;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.person.careerconnect.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    

    private final FileService fileService;
    
    public FileController(FileService fileService){
        this.fileService = fileService;
    }
    

    @Value("${careerconnect.upload-file.base-uri}")
    private String baseURI;

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException, StorageException{

        //Check file rỗng
        if(file == null || file.isEmpty()){
            throw new StorageException("File is empty. Please upload a file.");
        }

        //check định dạng file
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "xls", "xlsx");

        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
       if(!isValid){
        throw new StorageException("Invalid file extensions. Only allows "+ allowedExtensions.toString());
       }
       
        //Tạo folder nếu chưa có
        this.fileService.createDirectory(baseURI + folder);

        //Lưu trữ file
        String uploadFile = this.fileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

    return ResponseEntity.ok().body(res);
    }
}
