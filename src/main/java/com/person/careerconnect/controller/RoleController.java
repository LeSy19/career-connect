package com.person.careerconnect.controller;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.careerconnect.domain.Role;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.service.RoleService;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.person.careerconnect.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;
    
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException{
        //check name
        boolean isRoleExist = this.roleService.isRoleExist(role.getName());
        if(isRoleExist){
            throw new IdInvalidException("Role với name = "+role.getName()+" đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping
    @ApiMessage("update role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) throws IdInvalidException{
        //check id
        if(this.roleService.fetchById(role.getId()) == null){
            throw new IdInvalidException("Role với id = "+ role.getId() + " không tồn tại");
        }
        //check name
        // boolean isRoleExist = this.roleService.isRoleExist(role.getName());
        // if(isRoleExist){
        //     throw new IdInvalidException("Role với name = "+role.getName()+" đã tồn tại");
        // }

        return ResponseEntity.ok().body(this.roleService.update(role));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException{
        //check id
        if(this.roleService.fetchById(id) == null){
            throw new IdInvalidException("Role với id = "+ id + " không tồn tại");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }
    
    @GetMapping
    @ApiMessage("fetch all role")
    public ResponseEntity<ResultPaginationDTO> getAll(
        @Filter Specification<Role> spec,
        Pageable pageable
    ){
        return ResponseEntity.ok().body(this.roleService.fetchAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("fetch role by id")
    public ResponseEntity<Role> fetchRoleById(@PathVariable("id") long id) throws IdInvalidException{
        Role role = this.roleService.fetchById(id);
        if(role != null){
            throw new IdInvalidException("Role với id = "+id+"không tồn tại");
        }
        return ResponseEntity.ok().body(role);
    }
}
