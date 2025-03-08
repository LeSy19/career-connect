package com.person.careerconnect.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Job;
import com.person.careerconnect.domain.Permission;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.repository.PermissionRepository;

@Service
public class PermissionService {

    private PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission p) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(), p.getApiPath(), p.getMethod());
    }

    public Permission create(Permission p) {
        return this.permissionRepository.save(p);
    }

    public Permission fetchById(long id) {
        Optional<Permission> p = this.permissionRepository.findById(id);
        if (p.isPresent()) {
            return p.get();
        }
        return null;
    }

    public Permission update(Permission p) {
        Permission permisionDB = this.fetchById(p.getId());
        if (permisionDB != null) {
            permisionDB.setName(p.getName());
            permisionDB.setApiPath(p.getApiPath());
            permisionDB.setMethod(p.getMethod());
            permisionDB.setModule(p.getModule());

            permisionDB = this.permissionRepository.save(permisionDB);
            return permisionDB;
        }
        return null;
    }

    public void delete(long id) {
        // delete permission_role
        Optional<Permission> pOptional = this.permissionRepository.findById(id);
        Permission currentPermission = pOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete permission
        this.permissionRepository.delete(currentPermission);
    }

    public ResultPaginationDTO fetchAll(
            Specification<Permission> spec,
            Pageable pageable) {
        Page<Permission> pageSkill = this.permissionRepository.findAll(spec, pageable);

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

    public boolean isSameName(Permission p){
        Permission permissionDB = this.fetchById(p.getId());
        if(permissionDB != null){
            if(permissionDB.getName().equals(p.getName())){
                return true;
            }
        }
        return false;
    }
}
