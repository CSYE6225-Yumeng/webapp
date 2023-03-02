package com.yumeng.webapp.repository;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.Image;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.service.AmazonS3Service;
import com.yumeng.webapp.service.AmazonS3ServiceImpl;
import com.yumeng.webapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

@Component
@Repository
public class ImageReposity {
    @Autowired
    private AmazonS3ServiceImpl amazonS3Service;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    public Map<String, Object> createImage (MultipartFile file, String userid, Long productId) throws Exception {
        // upload imagefile to S3
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        // set
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        String path = String.format("%s/%s/%s", bucketName, "userID-"+userid, "productId-"+productId.toString());
        String fileName = String.format("%s", UUID.randomUUID()+"-"+file.getOriginalFilename());

        // Uploading file to s3
        PutObjectResult putObjectResult = amazonS3Service.upload(
                path, fileName, Optional.of(metadata), file.getInputStream());

        // save image metadata to database
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, userid);
        Image image = new Image();
        image.setProductId(productId.toString());
        image.setFileName(fileName);
        image.setS3BucketPath(path);
        image.setUser(user);
        session.save(image);
        transaction.commit();
        return image.getImageResponse();
    }

    public List<Image> getAllImages(String productId, String userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "FROM Image WHERE productId='"+productId+"' AND user.id="+userId;
        System.out.println(hql);
        List<Image> images = new ArrayList<Image>();
        images =session.createQuery(hql, Image.class).getResultList();
        transaction.commit();
        if(images.size() > 0){
            return images;
        }
        return null;
    }

    public Image hasPermission(String productId, String imageId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Image image = session.get(Image.class, imageId);
        transaction.commit();
        if(image != null){
            if(productId.equals(image.getProductId())){
                return image;
            }
        }
        return null;
    }

    public Image hasImage(String imageId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Image image = session.get(Image.class, imageId);
        transaction.commit();
        return image;
    }

    public void deleteImage(Image image) {
        // delete S3
        amazonS3Service.delete(image.getS3BucketPath(), image.getFileName());
        // delete metadata in database
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(image);
        transaction.commit();

    }
}
