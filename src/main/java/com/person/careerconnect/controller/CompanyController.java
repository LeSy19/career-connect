package com.person.careerconnect.controller;

import com.person.careerconnect.domain.Company;
import com.person.careerconnect.domain.dto.ResultPaginationDTO;
import com.person.careerconnect.service.CompanyService;
import com.person.careerconnect.service.error.IdInvalidException;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("Create company success")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws IdInvalidException {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(company));
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable
            ) {

        return ResponseEntity.ok().body(this.companyService.handleGetCompany(spec, pageable));
    }

    @PutMapping("/companies")
    @ApiMessage("Update company success")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company){
        return ResponseEntity.ok().body(this.companyService.handleUpdateCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete company success")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok().body(null);
    }
}
