package com.yumeng.webapp.controller;

import com.timgroup.statsd.StatsDClient;
import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.Image;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.repository.ImageReposity;
import com.yumeng.webapp.repository.ProductRepository;
import com.yumeng.webapp.service.MetadataService;
//import com.yumeng.webapp.service.MetadataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@RestController
public class ImageController {
    private ImageReposity imageReposity;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    private StatsDClient statsDClient;
    private ProductRepository productRepository;
//    private MetadataServiceImpl metadataService;
    public ImageController(ImageReposity imageReposity, ProductRepository productRepository) {
        this.imageReposity = imageReposity;
        this.productRepository = productRepository;
    }

    @PostMapping(
            value = "/v1/product/{productId}/image",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createImage(@PathVariable Long productId,
                                        @RequestBody MultipartFile imageFile,
                                        @RequestBody MultipartFile fileType,
                                        Principal principal) {
        statsDClient.incrementCounter("createImage.post");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[POST]create image request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // userId consist with productID
        Product getProduct = productRepository.hasPermission(userId, productId);
        if(getProduct == null) {
            logger.error("[POST]create image error: the productId doesn't exist in this user!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // save to S3 & save to database
        try {
            Map<String, Object> cImage = imageReposity.createImage(imageFile, userId, productId);
            logger.info("[POST]create image success");
            return ResponseEntity.status(HttpStatus.CREATED).body(cImage);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[POST]create image error: "+ e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/product/{productId}/image/{imageId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getImage(@PathVariable String productId,
                                      @PathVariable String imageId,
                                      Principal principal) {
        statsDClient.incrementCounter("getImage.get");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[GET]get image request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // userId consist with productID
        Product getProduct = productRepository.hasPermission(userId, Long.parseLong(productId));
        if(getProduct == null) {
            logger.error("[GET]get image error: the imageId doesn't match productId!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //productID consist with imageId
        Image getImage = imageReposity.hasPermission(productId, imageId);
        if(getImage == null) {
            logger.error("[GET]get image error: the imageId cannot find!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // get from S3
        try {
            logger.info("[GET]get image successful");
            return ResponseEntity.status(HttpStatus.OK).body(getImage.getImageResponse());
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[GET]get image error: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/product/{productId}/image",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getImages(@PathVariable String productId,
                                      Principal principal) {
        statsDClient.incrementCounter("getAllImages.get");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[GET]get all images request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // userId consist with productID
        Product getProduct = productRepository.hasPermission(userId, Long.parseLong(productId));
        if(getProduct == null) {
            logger.error("[GET]get all images error: productId doesn't exist!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //productID consist with imageId
        List<Image> images = imageReposity.getAllImages(productId, userId);
        if(images == null) {
            logger.error("[GET]get all images error: productId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // get from S3
        try {
            List<Map> result = new ArrayList<>();
            for(Image image: images){
                result.add(image.getImageResponse());
            }
            logger.info("[GET]get all images successful");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[GET]get all images error: "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }

    @DeleteMapping(value = "/v1/product/{productId}/image/{imageId}")  // @RequestBody User user
    public ResponseEntity deleteImage(
            @PathVariable String productId,
            @PathVariable String imageId,
            Principal principal) {
        statsDClient.incrementCounter("deleteImage.delete");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[DELETE]delete image request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403 & 404
        // userId consist with productID
        Product getProduct = productRepository.hasProduct(Long.parseLong(productId));
        if (getProduct == null){
            logger.error("[DELETE]delete image error: productId cannot find");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(! userId.equals(Long.toString(getProduct.getUser().getId()))){
            logger.error("[DELETE]delete image error: the productId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //productID consist with imageId
        Image getImage = imageReposity.hasImage(imageId);
        if (getImage == null){
            logger.error("[DELETE]delete image error: imageId cannot find");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(! userId.equals(Long.toString(getImage.getUser().getId()))){
            logger.error("[DELETE]delete image error: the imageId doesn't match productId!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // delete & 400 other error
        try {
            imageReposity.deleteImage(getImage);
            logger.info("[DELETE]delete image successful");
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[DELETE]delete image error: "+e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }



}
