package com.yumeng.webapp.controller;

import com.yumeng.webapp.config.SecurityConfiguration;
import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class ProductController {
    private ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping(
            value = "/v1/product",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProduct(@RequestBody Map<String,Object> params, Principal principal) {
        logger.info("[POST]create product request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // set & quantity
        Product product = new Product();
        for (Map.Entry<String, Object> entry: params.entrySet()){
            if (Objects.equals(entry.getKey(), "name")){
                product.setName(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "description")){
                product.setDescription(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "sku")){
                product.setSku(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "manufacturer")){
                product.setManufacturer(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "quantity")){
                if(! (entry.getValue() instanceof Integer)){
                    ErrorInfo errorInfo = new ErrorInfo(400, "Quantity must be a positive Integer less than or equal to 100!");
                    logger.error("[POST]create product error: Quantity must be a positive Integer less than or equal to 100!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(errorInfo);
                }else{
                    product.setQuantity(Integer.parseInt(entry.getValue().toString()));
                }
            } else {
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only contains name, description, sku, manufacturer and quantity in creating!");
                logger.error("[POST]create product error: You can only contains name, description, sku, manufacturer and quantity in creating!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }
        try {
            Map<String, Object> cProduct = productRepository.createProduct(product, userId);
            logger.info("[POST]create product successful");
            return ResponseEntity.status(HttpStatus.CREATED).body(cProduct);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.info("[POST]create product error: "+e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProduct(@PathVariable Long productId) {
        logger.info("[GET]get product request...");
        try {
            Map<String, Object> gProduct = productRepository.getProduct(productId);
            if (gProduct == null){
                ErrorInfo errorInfo = new ErrorInfo(404,"the product is null!");
                logger.error("[GET]get product error: the productId doesn't exist!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInfo);
            }
            logger.info("[GET]get product successful");
            return ResponseEntity.status(HttpStatus.OK).body(gProduct);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[GET]get product error: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }

    @PutMapping(
            value = "/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )  // @RequestBody User user
    public ResponseEntity updateProduct(@RequestBody Map<String,Object> params, @PathVariable Long productId, Principal principal) {
        logger.info("[PUT]update product request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        Product getProduct = productRepository.hasPermission(userId, productId);
        if(getProduct == null) {
            logger.error("[PUT]update product error: the productId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // set & quantity
        Product product = new Product();
        for (Map.Entry<String, Object> entry: params.entrySet()){
            if (Objects.equals(entry.getKey(), "name")){
                product.setName(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "description")){
                product.setDescription(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "sku")){
                product.setSku(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "manufacturer")){
                product.setManufacturer(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "quantity")){
                if(! (entry.getValue() instanceof Integer)){
                    ErrorInfo errorInfo = new ErrorInfo(400, "Quantity must be a positive Integer less than or equal to 100!");
                    logger.error("[PUT]update product error: Quantity must be a positive Integer less than or equal to 100!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(errorInfo);
                }else{
                    product.setQuantity(Integer.parseInt(entry.getValue().toString()));
                }
            } else {
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only update name, description, sku, manufacturer and quantity in database!");
                logger.error("[PUT]update product error: You can only update name, description, sku, manufacturer and quantity in database!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }
        if (product.getName() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            logger.error("[PUT]update product error: PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getDescription() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            logger.error("[PUT]update product error: PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getSku() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            logger.error("[PUT]update product error: PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getManufacturer() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            logger.error("[PUT]update product error: PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getQuantity() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            logger.error("[PUT]update product error: PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }

        // 400 other error
        try {
            Product uProduct = productRepository.updateProduct(product, getProduct);
            logger.info("[PUT]update product successful");
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[PUT]update product error: "+e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }


    @PatchMapping(
            value = "/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )  // @RequestBody User user
    public ResponseEntity updateProductPatch(@RequestBody Map<String,Object> params, @PathVariable Long productId, Principal principal) {
        logger.info("[PATCH]update product request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        Product getProduct = productRepository.hasPermission(userId, productId);
        if(getProduct == null) {
            logger.error("[PATCH]update product error: the productId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // set & quantity
        Product product = new Product();
        for (Map.Entry<String, Object> entry: params.entrySet()){
            if (Objects.equals(entry.getKey(), "name")){
                product.setName(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "description")){
                product.setDescription(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "sku")){
                product.setSku(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "manufacturer")){
                product.setManufacturer(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "quantity")){
                if(! (entry.getValue() instanceof Integer)){
                    ErrorInfo errorInfo = new ErrorInfo(400, "Quantity must be a positive Integer less than or equal to 100!");
                    logger.error("[PATCH]update product error: Quantity must be a positive Integer less than or equal to 100!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(errorInfo);
                }else{
                    product.setQuantity(Integer.parseInt(entry.getValue().toString()));
                }
            } else {
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only update name, description, sku, manufacturer and quantity in database!");
                logger.error("[PATCH]update product error: You can only update name, description, sku, manufacturer and quantity in database!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }
        // 400 other error
        try {
            Product uProduct = productRepository.updateProduct(product, getProduct);
            logger.info("[PATCH]update product successful");
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[PATCH]update product error: "+e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @DeleteMapping(value = "/v1/product/{productId}")  // @RequestBody User user
    public ResponseEntity deleteProduct(@PathVariable Long productId, Principal principal) {
        logger.info("[DELETE]delete product request...");
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403 & 404
        Product getProduct = productRepository.hasProduct(productId);
        if (getProduct == null){
            logger.error("[DELETE]delete product error: cannot find productID!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(! userId.equals(Long.toString(getProduct.getUser().getId()))){
            logger.error("[DELETE]delete product error: the productId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // delete & 400 other error
        try {
            productRepository.deleteProduct(getProduct);
            logger.info("[DELETE]delete product successful");
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

}
