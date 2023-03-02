package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.Image;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.repository.ImageReposity;
import com.yumeng.webapp.repository.ProductRepository;
import com.yumeng.webapp.service.MetadataService;
//import com.yumeng.webapp.service.MetadataServiceImpl;
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
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // userId consist with productID
        Product getProduct = productRepository.hasPermission(userId, productId);
        if(getProduct == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // save to S3 & save to database
        try {
            Map<String, Object> cImage = imageReposity.createImage(imageFile, userId, productId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cImage);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/product/{productId}/image/{imageId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getImage(@PathVariable String productId,
                                      @PathVariable String imageId,
                                      Principal principal) {
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // userId consist with productID
        Product getProduct = productRepository.hasPermission(userId, Long.parseLong(productId));
        if(getProduct == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //productID consist with imageId
        Image getImage = imageReposity.hasPermission(productId, imageId);
        if(getImage == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // get from S3
        try {
            return ResponseEntity.status(HttpStatus.OK).body(getImage.getImageResponse());
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/product/{productId}/image",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getImages(@PathVariable String productId,
                                      Principal principal) {
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // userId consist with productID
        Product getProduct = productRepository.hasPermission(userId, Long.parseLong(productId));
        if(getProduct == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //productID consist with imageId
        List<Image> images = imageReposity.getAllImages(productId, userId);
        if(images == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // get from S3
        try {
            List<Map> result = new ArrayList<>();
            for(Image image: images){
                result.add(image.getImageResponse());
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }

    @DeleteMapping(value = "/v1/product/{productId}/image/{imageId}")  // @RequestBody User user
    public ResponseEntity deleteProduct(
            @PathVariable String productId,
            @PathVariable String imageId,
            Principal principal) {
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403 & 404
        // userId consist with productID
        Product getProduct = productRepository.hasProduct(Long.parseLong(productId));
        if (getProduct == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(! userId.equals(Long.toString(getProduct.getUser().getId()))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //productID consist with imageId
        Image getImage = imageReposity.hasImage(imageId);
        if (getImage == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(! userId.equals(Long.toString(getImage.getUser().getId()))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // delete & 400 other error
        try {
            imageReposity.deleteImage(getImage);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }



}
