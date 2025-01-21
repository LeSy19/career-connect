package com.person.careerconnect.controller;

import com.person.careerconnect.domain.User;
import com.person.careerconnect.service.UserService;
import com.person.careerconnect.service.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/users")
    public ResponseEntity<User> CreatUser(@RequestBody User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = this.userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }



    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> CreatUser(@PathVariable("id") long id) throws IdInvalidException {
        if(id >= 100){
            throw new IdInvalidException("Id không lớn hơn 100");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok().body("Deleted");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        if(id >= 100){
            throw new IdInvalidException("Id không lớn hơn 100");
        }
        return ResponseEntity.ok().body(this.userService.findUserById(id));

    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUser(){
        return ResponseEntity.ok().body(this.userService.findAllUsers());

    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return ResponseEntity.ok().body(this.userService.updateUser(user));
    }
}
