package com.person.careerconnect.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.careerconnect.domain.Skill;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.repository.SkillRepository;
import com.person.careerconnect.service.SkillService;
import com.person.careerconnect.service.error.IdInvalidException;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    
    private final SkillService skillService;
    private final SkillRepository skillRepository;

    public SkillController(SkillService skillService, SkillRepository skillRepository){{
        this.skillService = skillService;
        this.skillRepository = skillRepository;
    }}

    @PostMapping("/skill")
    @ApiMessage("Create Skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        boolean existName = this.skillRepository.existsByName(skill.getName());
        if(existName){
            throw new IdInvalidException("Name already exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skill")
    @ApiMessage("Update Skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleUpdateSkill(skill);
        if(currentSkill == null){
            throw new IdInvalidException("Skill with id: "+skill.getId()+" not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleUpdateSkill(skill));
    }

    @GetMapping("/skill")
    @ApiMessage("Fetch all skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> spec,
                                                            Pageable pageable
            ) {
        return ResponseEntity.ok().body(this.skillService.handleGetSkill(spec, pageable));  
    }

}
