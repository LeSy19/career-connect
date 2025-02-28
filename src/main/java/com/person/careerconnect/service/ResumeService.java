package com.person.careerconnect.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Job;
import com.person.careerconnect.domain.Resume;
import com.person.careerconnect.domain.User;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.domain.response.resume.ResCreateResumeDTO;
import com.person.careerconnect.domain.response.resume.ResFetchResumeDTO;
import com.person.careerconnect.domain.response.resume.ResUpdateResumeDTO;
import com.person.careerconnect.repository.JobRepository;
import com.person.careerconnect.repository.ResumeRepository;
import com.person.careerconnect.repository.UserRepository;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume){
        //check user by id
        if(resume.getUser() == null){
            return false;
        }

        //check user by id
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if(userOptional.isEmpty()){
            return false;
        }

        //check job by id
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if(jobOptional.isEmpty()){
            return false;
        }

        return true;
    }

    public ResCreateResumeDTO createResume(Resume resume){
        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume.getId());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());

        return res;
    }

    public Optional<Resume> fetchById(long id){
        return this.resumeRepository.findById(id);
    }

    public ResUpdateResumeDTO updateResume(Resume resume){
        resume = this.resumeRepository.save(resume);

        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        return res;
    }

    public void deleleResume(long id){
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume){
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedBy(resume.getUpdatedBy());

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return res;
    }

    public ResultPaginationDTO getAllResume(Specification<Resume> spec, Pageable page){
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, page);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(page.getPageNumber() + 1);
        mt.setPageSize(page.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        //remove sensitive data
        List<ResFetchResumeDTO> listResumes = pageResume.getContent()
            .stream().map(item -> this.getResume(item)) 
            .collect(Collectors.toList());
            
        rs.setResult(listResumes);

        return rs;
    }
}
