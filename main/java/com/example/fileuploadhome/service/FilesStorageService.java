package com.example.fileuploadhome.service;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

public interface FilesStorageService{
    public void init();
    public void save(MultipartFile file);
    public Resource load(String filename);



}
