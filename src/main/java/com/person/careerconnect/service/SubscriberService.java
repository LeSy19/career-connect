package com.person.careerconnect.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Skill;
import com.person.careerconnect.domain.Subscriber;
import com.person.careerconnect.repository.SkillRepository;
import com.person.careerconnect.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository,
            SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber create(Subscriber subs){
        //check skill
        if(subs.getSkills() != null){
            List<Long> reqSkill = subs.getSkills()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill);
            subs.setSkills(dbSkill);
        }

        return this.subscriberRepository.save(subs);

    }

    public Subscriber findById(long id){
        Optional<Subscriber> subsOptional = this.subscriberRepository.findById(id);
        if(subsOptional != null){
            return subsOptional.get();
        }
        return null;
    }

    public Subscriber update(Subscriber subsDB, Subscriber subsRequest){
        //check skill
        if(subsRequest.getSkills() != null){
            List<Long> reqSkill = subsRequest.getSkills()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subsDB.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subsDB);
    }

}
