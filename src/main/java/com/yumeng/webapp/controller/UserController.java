package com.yumeng.webapp.controller;

import com.timgroup.statsd.StatsDClient;
import com.yumeng.webapp.config.AWSConfiguration;
import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private StatsDClient statsDClient;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @PostMapping(
            value = "/v2/user",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody User user) {
        statsDClient.incrementCounter("endpoint.createUser.post");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[POST]create user request...");
        if (user.emailValidation()){
            try {
                User cUser = userRepository.createUser(user);
                logger.info("[POST]create user successful!");
                return ResponseEntity.status(HttpStatus.CREATED).body(cUser);
            }catch (Exception e){
//                String detail = ((ConstraintViolationException) e).getSQLException().getMessage();
                logger.error("[POST]create user error:"+e.getMessage());
                ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
                return ResponseEntity.badRequest().body(errorInfo);
            }
        }else{
            ErrorInfo errorInfo = new ErrorInfo(400, "invaild username(invaild email-address)!");
            logger.error("[POST]create user error: invaild username(invaild email-address)!");
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable Long userId, Principal principal) {
        statsDClient.incrementCounter("getUser.get");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[GET]get user request...");
        String authId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        if(authId.equals(Long.toString(userId))) {
            try {
                User gUser = userRepository.getUsers(userId);
                logger.info("[GET]get user successful!");
                return ResponseEntity.status(HttpStatus.OK).body(gUser);
            }catch (Exception e){
                ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
                logger.error("[GET]get user error:"+e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
            }
        }else{
            logger.error("[GET]get user error: the userId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )  // @RequestBody User user
    public ResponseEntity updateUser(@RequestBody Map<String,Object> params, @PathVariable Long userId, Principal principal) {
        statsDClient.incrementCounter("updateUser.put");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[PUT]update user request...");
        String authId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        if(!authId.equals(Long.toString(userId))) {
            logger.error("[PUT]update user error: the userId doesn't match auth!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 400 Attempt to update any other field (except First Name, Last Name and Password)
        User user = new User();
        for (Map.Entry<String, Object> entry: params.entrySet()){
            if (Objects.equals(entry.getKey(), "first_name")){
                user.setFirstName(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "last_name")){
                user.setLastName(entry.getValue().toString());
            }else if(Objects.equals(entry.getKey(), "password")){
                user.setPassword(entry.getValue().toString());
            } else{
                ErrorInfo errorInfo = new ErrorInfo(400, "You can only update first_name, last_name and password in database!");
                logger.error("[PUT]update user error: You can only update first_name, last_name and password in database!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }

        // 400 other error
        try {
            User newUser = userRepository.updateUsers(userId, user);
            logger.info("[PUT]update user successful!");
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            logger.error("[PUT]update user error:"+e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }


    }


}
