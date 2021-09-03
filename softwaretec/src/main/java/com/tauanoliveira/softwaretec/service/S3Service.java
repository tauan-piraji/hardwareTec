package com.tauanoliveira.softwaretec.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tauanoliveira.softwaretec.service.exception.FileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);
    
    @Autowired
    private AmazonS3 s3Amazon;

    @Value("${s3.bucket}")
    private String BucketName;

    public URI uploadFile(MultipartFile multipartFile) {//URI retorna endereço web do envio
            try {
                String fileName = multipartFile.getOriginalFilename();//pega o nome do arquivo
                InputStream is = multipartFile.getInputStream();
                String contentType = multipartFile.getContentType();//pega o tipo do arquivo(.jpg, .png, .text etc)
                return uploadFile(is, fileName, contentType);
            } catch (IOException e) {
                throw new FileException("erro de IO: " + e.getMessage());
            }
    } 

    public URI uploadFile(InputStream is, String fileName, String contentType) {
        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            LOG.info("Iniciando upload");
            s3Amazon.putObject(BucketName, fileName, is, metadata);
            LOG.info("Upload realizado com sucesso");
            return s3Amazon.getUrl(BucketName, fileName).toURI();
        }catch(URISyntaxException e){
            throw new FileException("erro em converter URL para URI");
        }
        
    }
}