package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.repository.UserRepository;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
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
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(
            value = "/v1/user",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody User user) {
        if (user.emailValidation()){
            try {
                User cUser = userRepository.createUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(cUser);
            }catch (Exception e){
//                String detail = ((ConstraintViolationException) e).getSQLException().getMessage();
                ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
                return ResponseEntity.badRequest().body(errorInfo);
            }
        }else{
            ErrorInfo errorInfo = new ErrorInfo(400, "invaild username(invaild email-address)!");
            return ResponseEntity.badRequest().body(errorInfo);
        }
    }

    @GetMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable Long userId, Principal principal) {
        String authId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        if(authId.equals(Long.toString(userId))) {
            try {
                User gUser = userRepository.getUsers(userId);
                return ResponseEntity.status(HttpStatus.OK).body(gUser);
            }catch (Exception e){
                ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
            }
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )  // @RequestBody User user
    public ResponseEntity updateUser(@RequestBody Map<String,Object> params, @PathVariable Long userId, Principal principal) {
        String authId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        if(!authId.equals(Long.toString(userId))) {
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorInfo);
            }
        }

        // 400 other error
        try {
            User newUser = userRepository.updateUsers(userId, user);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo(400, e.getMessage());
            return ResponseEntity.badRequest().body(errorInfo);
        }


    }


}
