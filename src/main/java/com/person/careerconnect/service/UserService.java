package com.person.careerconnect.service;

import com.person.careerconnect.domain.User;
import com.person.careerconnect.domain.dto.Meta;
import com.person.careerconnect.domain.dto.ResultPaginationDTO;
import com.person.careerconnect.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public ResultPaginationDTO findAllUsers(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
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
