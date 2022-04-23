package com.example.tmdt_be.service;

import com.example.tmdt_be.service.sdo.AmazonUploadSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmazonUploadService {
    AmazonUploadSdo uploadMultiFile(List<MultipartFile> files) throws JsonProcessingException ;

    String uploadFile(MultipartFile multipartFile) throws JsonProcessingException;

    Boolean isExists(String fileName) throws JsonProcessingException;
}
