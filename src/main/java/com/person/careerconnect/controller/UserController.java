package com.person.careerconnect.controller;

import com.person.careerconnect.domain.User;
import com.person.careerconnect.domain.response.ResCreateUserDTO;
import com.person.careerconnect.domain.response.ResUpdateUserDTO;
import com.person.careerconnect.domain.response.ResUserDTO;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.service.UserService;
import com.person.careerconnect.service.error.IdInvalidException;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")

public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/users")
    @ApiMessage("Create user success")
    public ResponseEntity<ResCreateUserDTO> CreatUser(@Valid @RequestBody User user) throws IdInvalidException {

        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if(isEmailExist){
            throw new IdInvalidException("Email "+ user.getEmail() +"đã tồn tại, vui lòng sử dụng email khác");
        }

        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }



    @DeleteMapping("/users/{id}")
    @ApiMessage("delete user success")
    public ResponseEntity<Void> CreatUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.findUserById(id);
        if(currentUser == null){
            throw new IdInvalidException("User với id: "+currentUser.getId()+" không tồn tại!");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch users by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.findUserById(id);
        if(fetchUser == null){
            throw new IdInvalidException("User với id: "+fetchUser.getId()+" không tồn tại!");
        }
        return ResponseEntity.ok().body(this.userService.convertToResUserDTO(fetchUser));

    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> findAllUser(
            @Filter Specification<User> spec,
           Pageable pageable
            ){

        return ResponseEntity.ok().body(this.userService.findAllUsers(spec, pageable));

    }

    @PutMapping("/users")
    @ApiMessage("Update user success")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User currentUser = this.userService.updateUser(user);
        if(currentUser == null){
            throw new IdInvalidException("User với id = "+user.getId()+" không tồn tại");
        }
        return ResponseEntity.ok().body(this.userService.convertUpdateUserDTO(currentUser));
    }
}
