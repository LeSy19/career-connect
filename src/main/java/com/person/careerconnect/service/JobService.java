package com.person.careerconnect.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Job;
import com.person.careerconnect.domain.Skill;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.domain.response.job.ResCreateJobDTO;
import com.person.careerconnect.domain.response.job.ResUpdateJobDTO;
import com.person.careerconnect.repository.JobRepository;
import com.person.careerconnect.repository.SkillRepository;

@Service
public class JobService {
    
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository){
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public Optional<Job> getJobById(long id){
        Optional<Job> job = this.jobRepository.findById(id);
        if(job.isPresent()){
            return job;
        }
        return null;
    }

    public ResCreateJobDTO createJob(Job job){
        //check skill
        if(job.getSkills() != null){
            List<Long> reqSkill = job.getSkills()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkills);
        }

        //create job
        Job currentJob = this.jobRepository.save(job);

        //convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
            .stream().map(x -> x.getName())
            .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;

    }

    public ResUpdateJobDTO updateJob(Job job){
         //check skill
         if(job.getSkills() != null){
            List<Long> reqSkill = job.getSkills()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkills);
        }

        //create job
        Job currentJob = this.jobRepository.save(job);

        //convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
            .stream().map(x -> x.getName())
            .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }

    public void deleteJob(long id){
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllJobs(Specification<Job> spec, Pageable pageable){
         Page<Job> pageSkill = this.jobRepository.findAll(spec, pageable);

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
