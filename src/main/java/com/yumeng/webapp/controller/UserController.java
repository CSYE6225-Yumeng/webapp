package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.User;
import com.yumeng.webapp.repository.UserRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }else{
            return ResponseEntity.badRequest().body("invaild username(invaild email-address)!");
        }
    }

    @GetMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable long userId, Principal principal) {
        String authId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        if(authId.equals(Long.toString(userId))) {
            try {
                User gUser = userRepository.getUsers(userId);
                return ResponseEntity.status(HttpStatus.OK).body(gUser);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable long userId, Principal principal) {
        String authId = ((UsernamePasswordAuthenticationToken) principal).getAuthorities().toArray()[0].toString();
        // 403
        if(!authId.equals(Long.toString(userId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 400 Attempt to update any other field (except First Name, Last Name and Password)
        if (Objects.isNull(user.getId()) || Objects.isNull(user.getUsername()) ||
                Objects.isNull(user.getAccountUpdated()) || Objects.isNull(user.getAccountUpdated()) ||
                Objects.isNull(user.getAuthorities()) ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You can only update first_name, last_name and password in database!");
        }
        // 400 username vaildation
        try {
            User newUser = userRepository.updateUsers(userId, user);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }


}
