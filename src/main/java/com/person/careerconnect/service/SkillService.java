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

    public boolean isExistsName(String name){
        return this.skillRepository.existsByName(name);
    }

    public Skill fetchSkillById(long id){
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if(skillOptional.isPresent()){
            return skillOptional.get();
        }
        return null;
    }

   
    public Skill handleCreateSkill(Skill skill){
        return this.skillRepository.save(skill);
    }

    public Skill handleUpdateSkill(Skill skill){
        return this.skillRepository.save(skill);
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

    public void deleteSkill(long id){
        //delete job (insite job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
 
        //delete skill
        this.skillRepository.deleteById(id);
    }


}
