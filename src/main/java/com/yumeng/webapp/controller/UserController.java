package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.User;
import com.yumeng.webapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        User cUser = userRepository.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(cUser);
    }

    @GetMapping(
            value = "/v1/user/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@RequestBody User user, @PathVariable long userId) {
        User gUser = userRepository.getUsers(userId);
        return ResponseEntity.status(HttpStatus.OK).body(gUser);
    }

    @PutMapping(
            value = "/v1/user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable long userId) {
        User newUser = userRepository.updateUsers(userId, user);
        return ResponseEntity.noContent().build();
//        return ResponseEntity.badRequest().build();
//        return ResponseEntity.unauthorized().build();
        //return ResponseEntity.Forbidden().build();
    }


}
