package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.Product;
import com.yumeng.webapp.repository.ProductRepository;
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
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping(
            value = "/v1/product",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProduct(@RequestBody Map<String,Object> params, Principal principal) {
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(errorInfo);
                }else{
                    product.setQuantity(Integer.parseInt(entry.getValue().toString()));
                }
            } else {
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only contains name, description, sku, manufacturer and quantity in creating!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }
        try {
            Map<String, Object> cProduct = productRepository.createProduct(product, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cProduct);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProduct(@PathVariable Long productId) {
        try {
            Map<String, Object> gProduct = productRepository.getProduct(productId);
            if (gProduct == null){
                ErrorInfo errorInfo = new ErrorInfo(404,"the product is null!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInfo);
            }
            return ResponseEntity.status(HttpStatus.OK).body(gProduct);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }

    @PutMapping(
            value = "/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )  // @RequestBody User user
    public ResponseEntity updateProduct(@RequestBody Map<String,Object> params, @PathVariable Long productId, Principal principal) {
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        Product getProduct = productRepository.hasPermission(userId, productId);
        if(getProduct == null) {
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(errorInfo);
                }else{
                    product.setQuantity(Integer.parseInt(entry.getValue().toString()));
                }
            } else {
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only update name, description, sku, manufacturer and quantity in database!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }
        if (product.getName() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getDescription() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getSku() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getManufacturer() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }
        if (product.getQuantity() == null){
            ErrorInfo errorInfo = new ErrorInfo(400, "PUT request ask for all required fields: name, description, sku, manufacturer and quantity!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorInfo);
        }

        // 400 other error
        try {
            Product uProduct = productRepository.updateProduct(product, getProduct);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }


    @PatchMapping(
            value = "/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )  // @RequestBody User user
    public ResponseEntity updateProductPatch(@RequestBody Map<String,Object> params, @PathVariable Long productId, Principal principal) {
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        Product getProduct = productRepository.hasPermission(userId, productId);
        if(getProduct == null) {
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(errorInfo);
                }else{
                    product.setQuantity(Integer.parseInt(entry.getValue().toString()));
                }
            } else {
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only update name, description, sku, manufacturer and quantity in database!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }
        // 400 other error
        try {
            Product uProduct = productRepository.updateProduct(product, getProduct);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @DeleteMapping(value = "/v1/product/{productId}")  // @RequestBody User user
    public ResponseEntity deleteProduct(@PathVariable Long productId, Principal principal) {
        String userId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403 & 404
        Product getProduct = productRepository.hasProduct(productId);
        if (getProduct == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(! userId.equals(Long.toString(getProduct.getUser().getId()))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // delete & 400 other error
        try {
            productRepository.deleteProduct(getProduct);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

}
