package com.example.tmdt_be.controller;

import com.example.tmdt_be.service.AmazonUploadService;
import com.example.tmdt_be.service.sdo.AmazonUploadSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="uploads")
public class AmazonUploadController {
    @Autowired
    AmazonUploadService amazonUploadService;

    @PostMapping(path = "/uploadFileMulti",consumes = {"multipart/form-data"})
    public ResponseEntity<AmazonUploadSdo> uploadMultiFile(@RequestPart(value = "files", required = false) List<MultipartFile> files) throws JsonProcessingException {
        return ResponseEntity.ok(amazonUploadService.uploadMultiFile(files));
    }
}
