package com.yumeng.webapp.controller;

import com.yumeng.webapp.data.ErrorInfo;
import com.yumeng.webapp.data.User;
import com.yumeng.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private User testUser = new User();
    private User createReturnUser = new User();
    private UserRepository testUserRepository = mock(UserRepository.class);
    private UserController testUserController = new UserController(testUserRepository);

    @BeforeEach
    void setUp() throws ParseException {
        testUser.setFirstName("Jane");
        testUser.setLastName("Doe");
        testUser.setPassword("somepassword");
        testUser.setUsername("jane.doe@gmail.com");

        createReturnUser.setFirstName("Jane");
        createReturnUser.setLastName("Doe");
        createReturnUser.setUsername("jane.doe@gmail.com");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.ENGLISH);
        String dateInString = "22-01-2015 10:15:55 AM";
        Date date = formatter.parse(dateInString);
        createReturnUser.setAccountCreated(date);
        createReturnUser.setAccountUpdated(date);
        createReturnUser.setId(1L);
    }

    @Test
    void createUser() throws PSQLException {
        when(testUserRepository.createUser(testUser)).thenReturn(createReturnUser);
        ResponseEntity actualUser = testUserController.createUser(testUser);
        assertEquals(createReturnUser, actualUser.getBody());
    }

    @Test
    void createUserInvaildEmail() throws PSQLException {
        testUser.setUsername("45637899");
        ResponseEntity actualUser = testUserController.createUser(testUser);
        assertEquals(HttpStatus.BAD_REQUEST, actualUser.getStatusCode());
        assertEquals("invaild username(invaild email-address)!", ((ErrorInfo) actualUser.getBody()).getErrorMessage());
    }

    @Test
    void createUserSameUsername() throws PSQLException {
        PSQLException sameUsername = new PSQLException("same username!", PSQLState.UNIQUE_VIOLATION);
        when(testUserRepository.createUser(testUser)).thenThrow(sameUsername);
        ResponseEntity actualUser = testUserController.createUser(testUser);
        assertEquals(HttpStatus.BAD_REQUEST, actualUser.getStatusCode());
        assertEquals("same username!", ((ErrorInfo) actualUser.getBody()).getErrorMessage());
    }

    @Test
    void getUser() {
    }

    @Test
    void updateUser() {
    }
}