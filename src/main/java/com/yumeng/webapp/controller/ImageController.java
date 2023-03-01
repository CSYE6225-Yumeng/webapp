package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.Image;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.repository.ImageReposity;
import com.yumeng.webapp.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class ImageController {
    private ImageReposity imageReposity;
    private ProductRepository productRepository;
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
        // set image
        Image image = new Image();
//        MultipartFile imageBinary = null;
//        String fileType = null;
//        for (Map.Entry<String, Object> entry: params.entrySet()){
//            if (Objects.equals(entry.getKey(), "file")){
//                imageBinary = entry.getValue().toString();
//            }else if(Objects.equals(entry.getKey(), "fileType")){
//                fileType = entry.getValue().toString();
//            }else {
//                ErrorInfo errorInfo = new ErrorInfo(400, "You can only contains file and fileType in creating!");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(errorInfo);
//            }
//        }
        if(imageFile == null ||fileType == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "You must contains file and fileType in creating!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        // TODO save to S3
//        image.setImageId();
        image.setProductId(productId.toString());
        image.setFileName("name");
        image.setS3BucketPath("/test/test/test");
        try {
            Map<String, Object> cProduct = imageReposity.createImage(image, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cProduct);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

}
