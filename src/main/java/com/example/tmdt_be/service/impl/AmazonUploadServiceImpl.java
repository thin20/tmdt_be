package com.example.tmdt_be.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.tmdt_be.service.AmazonUploadService;
import com.example.tmdt_be.service.exception.AppException;
import com.example.tmdt_be.service.sdi.MediaFileSdi;
import com.example.tmdt_be.service.sdo.AmazonUploadSdo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonServiceException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AmazonUploadServiceImpl implements AmazonUploadService {
    private AmazonS3 s3client;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, MultipartFile multipartFile) throws JsonProcessingException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        try {
            s3client.putObject(bucketName, fileName, multipartFile.getInputStream(), metadata);
        }catch (IOException e){
            throw new AppException("API-UPL001", "Upload files thất bại!");
        }
    }

    @Override
    public AmazonUploadSdo uploadMultiFile(List<MultipartFile> files) throws JsonProcessingException {
        List<MediaFileSdi> filesUploads = new ArrayList<>();

        if (files == null) return null;

        try {
            for (MultipartFile file : files){
                MediaFileSdi fileUpload = new MediaFileSdi();
                fileUpload.setFilePath(this.uploadFile(file));
                fileUpload.setFileLength(file.getSize());
                fileUpload.setFileType(file.getContentType());
                filesUploads.add(fileUpload);
            }
        } catch(Exception e) {
            throw new AppException("API-UPL001", "Upload files thất bại!");
        }

        AmazonUploadSdo result = new AmazonUploadSdo();
        result.setFilePath(filesUploads);

        return result;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws JsonProcessingException {
        String fileUrl = "";
        try {
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            if (this.isExists(fileName) == false) {
                uploadFileTos3bucket(fileName, multipartFile);
            }
        } catch (Exception e) {
            throw new AppException("API-UPL001", "Upload files thất bại!");
        }
        return fileUrl;
    }

    @Override
    public Boolean isExists(String fileName) {
        try {
            s3client.getObjectMetadata(bucketName, endpointUrl + "/" + bucketName + "/" + fileName);
        } catch(AmazonServiceException e) {
            return false;
        }
        return true;
    }
}
