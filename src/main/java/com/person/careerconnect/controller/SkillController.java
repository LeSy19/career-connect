package com.person.careerconnect.controller;

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

import com.person.careerconnect.domain.Skill;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.repository.SkillRepository;
import com.person.careerconnect.service.SkillService;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.person.careerconnect.util.error.IdInvalidException;
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

    @PostMapping("/skills")
    @ApiMessage("Create Skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        //check name
        if(skill.getName() != null && this.skillService.isExistsName(skill.getName())){
            throw new IdInvalidException("Skill name = "+skill.getName()+" đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update Skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {

        //check id
        Skill currentSkill = this.skillService.fetchSkillById(skill.getId());
        if(currentSkill == null){
            throw new IdInvalidException("Skill id = "+skill.getId()+" không tồn tại");
        }

        //check name
        if(skill.getName() != null && this.skillService.isExistsName(skill.getName())){
            throw new IdInvalidException("Skill name = "+skill.getName()+" đã tồn tại");
        }

        currentSkill.setName(skill.getName());
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleUpdateSkill(skill));
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> spec,
                                                            Pageable pageable
            ) {
        return ResponseEntity.ok().body(this.skillService.handleGetSkill(spec, pageable));  
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete Skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException{
        //check id
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if(currentSkill == null){
            throw new IdInvalidException("Skill id = "+id+" không tồn tại");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);

    }

}
