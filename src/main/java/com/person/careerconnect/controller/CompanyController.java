package com.person.careerconnect.controller;

import com.person.careerconnect.domain.Company;
import com.person.careerconnect.service.CompanyService;
import com.person.careerconnect.service.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws IdInvalidException {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(company));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok().body(this.companyService.handleGetCompany());
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company){
        return ResponseEntity.ok().body(this.companyService.handleUpdateCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok().body(null);
    }
}
