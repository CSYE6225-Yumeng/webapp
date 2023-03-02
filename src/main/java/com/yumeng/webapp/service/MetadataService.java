package com.yumeng.webapp.service;

import com.amazonaws.services.s3.model.S3Object;

import com.yumeng.webapp.data.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MetadataService {
    public void upload(MultipartFile file) throws IOException;
    public S3Object download(int id);
    public List<Image> list();
}