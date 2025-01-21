package com.person.careerconnect.service;

import com.person.careerconnect.domain.User;
import com.person.careerconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        userRepository.save(user);
        return user;
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    public User findUserById(long id){
        Optional<User> user = this.userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        }

        return null;
    }

    public List<User> findAllUsers(){
        return this.userRepository.findAll();
    }

    public User updateUser(User user){
        User currentUser = this.findUserById(user.getId());
        if(currentUser != null){
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username){
       return this.userRepository.findByEmail(username);
    }
}
