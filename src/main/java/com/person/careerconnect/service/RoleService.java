package com.person.careerconnect.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Permission;
import com.person.careerconnect.domain.Role;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.repository.PermissionRepository;
import com.person.careerconnect.repository.RoleRepository;

@Service
public class RoleService {

    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean isRoleExist(String name){
        return this.roleRepository.existsByName(name);
    }

    public Role createRole(Role role){

        //check permission
        if(role.getPermissions() != null){
            List<Long> reqPermission = role.getPermissions()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(role);
    }

    public Role fetchById(long id){
        Optional<Role> rOptional = this.roleRepository.findById(id);
        if(rOptional.isPresent()){
            return rOptional.get();
        }
        return null;
    }

    public Role update(Role role){
        Role roleDB = this.fetchById(role.getId());
        //check permission
        if(role.getPermissions() != null){
            List<Long> reqPermission = role.getPermissions()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(dbPermissions);
        }

        roleDB.setName(role.getName());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.isActive());
        roleDB.setPermissions(role.getPermissions());

        roleDB = this.roleRepository.save(roleDB);
        return roleDB;

    }

    public void delete(long id){
        this.roleRepository.deleteById(id);
    }
    

    public ResultPaginationDTO fetchAll(Specification<Role> spec, Pageable pageable){
        Page<Role> pageSkill = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;
    }
}
