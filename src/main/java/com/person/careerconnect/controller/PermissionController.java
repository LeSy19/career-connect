package com.person.careerconnect.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.careerconnect.domain.Permission;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.service.PermissionService;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.person.careerconnect.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission p) throws IdInvalidException{

        //check permission
        if(this.permissionService.isPermissionExist(p)){
            throw new IdInvalidException("Permission đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(p));
    }

    @PutMapping
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission p) throws IdInvalidException{
        //check id
        if(this.permissionService.fetchById(p.getId()) == null){
            throw new IdInvalidException("Id = " + p.getId() + " không tồn tại");
        }

        //check permission
        if(this.permissionService.isPermissionExist(p)){
            throw new IdInvalidException("Permission đã tồn tại");
        }

        return ResponseEntity.ok().body(this.permissionService.update(p));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException{
        //check id
        if(this.permissionService.fetchById(id) == null){
            throw new IdInvalidException("Id = " + id + " không tồn tại");
        }

        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    @ApiMessage("Get all permissions")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
        @Filter Specification<Permission> spec,
        Pageable page
    ){
        return ResponseEntity.ok().body(this.permissionService.fetchAll(spec, page));
    }
}
