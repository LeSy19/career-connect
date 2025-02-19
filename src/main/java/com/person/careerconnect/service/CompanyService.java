package com.person.careerconnect.service;

import com.person.careerconnect.domain.Company;
import com.person.careerconnect.domain.User;
import com.person.careerconnect.domain.response.ResultPaginationDTO;
import com.person.careerconnect.repository.CompanyRepository;
import com.person.careerconnect.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
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

        Optional<Company> comOptional = this.companyRepository.findById(id);
        if(comOptional.isPresent()){
            Company com = comOptional.get();
            //Lấy tất cả người dùng thuộc công ty
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }

        this.companyRepository.deleteById(id);
    }

    public Optional<Company> findById(long id){
        return this.companyRepository.findById(id);
    }
}
