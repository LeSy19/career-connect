package com.person.careerconnect.service;

import com.person.careerconnect.domain.Company;
import com.person.careerconnect.domain.dto.CompanyDTO;
import com.person.careerconnect.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> handleGetCompany() {
        return this.companyRepository.findAll();
    }
    public Company handleUpdateCompany(Company company) {
        Optional<Company> CompanyOptional = this.companyRepository.findById(company.getId());
        if(CompanyOptional.isPresent()) {
            Company currentCompany = CompanyOptional.get();
            currentCompany.setName(company.getName());
            currentCompany.setAddress(company.getAddress());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setLogo(company.getLogo());
            currentCompany = companyRepository.save(currentCompany);
            return currentCompany;
        }
        return null;

    }


    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
