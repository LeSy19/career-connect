package com.person.careerconnect.controller;

import java.util.Optional;

import org.hibernate.query.Page;
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

import com.person.careerconnect.domain.Job;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.domain.response.job.ResCreateJobDTO;
import com.person.careerconnect.domain.response.job.ResUpdateJobDTO;
import com.person.careerconnect.service.JobService;
import com.person.careerconnect.service.error.IdInvalidException;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    
    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create Job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.createJob(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update Job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job)throws IdInvalidException{

        //check id
        Optional<Job> currentJob = this.jobService.getJobById(job.getId());
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Job id = "+job.getId()+" không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.updateJob(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete Job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.getJobById(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Job id = "+id+" không tồn tại");
        }

        this.jobService.deleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("Fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> fetchAllJobs(@Filter Specification<Job> spec,
                                            Pageable pageable){

    return ResponseEntity.ok().body(this.jobService.fetchAllJobs(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch job by id")
    public ResponseEntity<Job> fetchJobById(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Job> currentjob = this.jobService.getJobById(id);
        if(!currentjob.isPresent()){
            throw new IdInvalidException("Job id = "+id+" không tồn tại");
        }
        return ResponseEntity.ok().body(currentjob.get());
    }
}
