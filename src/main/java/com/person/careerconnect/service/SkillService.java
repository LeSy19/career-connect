package com.person.careerconnect.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Skill;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    }

   
    public Skill handleCreateSkill(Skill skill){
        return this.skillRepository.save(skill);
    }

    public Skill handleUpdateSkill(Skill skill){
        Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
        if(skillOptional.isPresent()){
            Skill currentSkill = skillOptional.get();
            currentSkill.setName(skill.getName());
            currentSkill = skillRepository.save(currentSkill);
            return currentSkill;
        }
        return null;
    }

    public Optional<Skill> getById(long id){
        return this.skillRepository.findById(id);
    }

    public ResultPaginationDTO handleGetSkill(Specification<Skill> spec, Pageable pageable){
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);

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
