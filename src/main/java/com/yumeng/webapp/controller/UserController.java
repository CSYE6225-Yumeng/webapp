package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.User;
import com.yumeng.webapp.repository.UserRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

//    @Autowired
//    private JdbcUserDetailsManager userDetailsManager;
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
//                userDetailsManager.createUser(new SecureUser(cUser));
                return ResponseEntity.status(HttpStatus.CREATED).body(cUser);
            }catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(
            value = "/v1/user/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable long userId, Principal principal) {
        User gUser = userRepository.getUsers(userId);
        return ResponseEntity.status(HttpStatus.OK).body(gUser);
    }

    @PutMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable long userId) {
        if(user.getUsername() != null){
            if (user.emailValidation()){
                try {
                    User newUser = userRepository.updateUsers(userId, user);
                    return ResponseEntity.noContent().build();
                }catch (PSQLException e){
                    return ResponseEntity.badRequest().build();
                }
            }else{
                return ResponseEntity.badRequest().build();
            }
        }else{
            try {
                User newUser = userRepository.updateUsers(userId, user);
                return ResponseEntity.noContent().build();
            }catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }
//        return ResponseEntity.badRequest().build();
//        return ResponseEntity.unauthorized().build();
        //return ResponseEntity.Forbidden().build();
    }


}
