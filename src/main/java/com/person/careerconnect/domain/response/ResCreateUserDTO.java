package com.person.careerconnect.domain.response;

import com.person.careerconnect.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private CompanyUser company;

    private String address;

    private Instant createdAt;


    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }
}
