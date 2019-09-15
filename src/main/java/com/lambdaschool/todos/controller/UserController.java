package com.lambdaschool.todos.controller;

import com.lambdaschool.todos.model.User;
import com.lambdaschool.todos.service.UserService;
import com.lambdaschool.todos.views.CountTodos;
import com.lambdaschool.todos.views.UserAndTodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//    // GET localhost:2019/users/users
//    @GetMapping(value = "/users", produces = {"application/json"})
//    public ResponseEntity<?> listAllUsers() {
//        List<User> myUsers = userService.findAll();
//        return new ResponseEntity<>(myUsers, HttpStatus.OK);
//    }
//
    // GET localhost:2019/users/user/2
    @GetMapping(value = "/user/{userId}", produces = {"application/json"})
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        User u = userService.findUserById(userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    // GET localhost:2019/users/mine
//    @GetMapping(value = "/mine", produces = {"application/json"})
//    public ResponseEntity<?> getUser( {
//        User u = userService.findUserById();
//        return new ResponseEntity<>(u, HttpStatus.OK);
//    }


//  localhost:2019/users/mine
    @GetMapping(value = "/mine", produces = {"application/json"})
    public ResponseEntity<?> getUserAndTodos() {
        User myList = userService.findUserAndTodos();
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }


    // POST localhost:2019/users/user/
    @PostMapping(value = "/user", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User newuser) throws URISyntaxException {
        newuser = userService.save(newuser);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newuser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User updateUser, @PathVariable long id) {
        userService.update(updateUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}