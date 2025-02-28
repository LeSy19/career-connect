package com.person.careerconnect.controller;

import java.util.Optional;

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

import com.person.careerconnect.domain.Resume;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.domain.response.resume.ResCreateResumeDTO;
import com.person.careerconnect.domain.response.resume.ResFetchResumeDTO;
import com.person.careerconnect.domain.response.resume.ResUpdateResumeDTO;
import com.person.careerconnect.service.ResumeService;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.person.careerconnect.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException{
        //check id 
        boolean isIdExists = this.resumeService.checkResumeExistByUserAndJob(resume);
        if(!isIdExists){
            throw new IdInvalidException("User id/Job id không tồn tại");
        }

        //create resume
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.createResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException{
        //check id 
        Optional<Resume> resumeOptional = this.resumeService.fetchById(resume.getId());
        if(resumeOptional.isEmpty()){
            throw new IdInvalidException("Resume với id = "+resume.getId()+" không tồn tại");
        }
        
        Resume reqResume = resumeOptional.get();
        reqResume.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.updateResume(reqResume));    
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException{
        //check id
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if(resumeOptional.isEmpty()){
            throw new IdInvalidException("Resume với id = "+id+" không tồn tại");
        }

        this.resumeService.deleleResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume")
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable("id") long id) throws IdInvalidException{
        //check id
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if(resumeOptional.isEmpty()){
            throw new IdInvalidException("Resume với id = "+id+" không tồn tại");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(resumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
        @Filter Specification<Resume> spec,
        Pageable pageable
    ){
        return ResponseEntity.ok().body(this.resumeService.getAllResume(spec, pageable));
    }
}
