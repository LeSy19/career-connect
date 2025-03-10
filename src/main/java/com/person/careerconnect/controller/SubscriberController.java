package com.person.careerconnect.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.careerconnect.domain.Subscriber;
import com.person.careerconnect.service.SecurityUtil;
import com.person.careerconnect.service.SubscriberService;
import com.person.careerconnect.util.annotation.ApiMessage;
import com.person.careerconnect.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1") 
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }
    
    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber subs) throws IdInvalidException{
        //check email
        boolean isExists = this.subscriberService.isExistsByEmail(subs.getEmail());
        if(isExists == true){
            throw new IdInvalidException("Email = "+subs.getEmail()+" đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(subs));
        }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> update(@Valid @RequestBody Subscriber subsRequest) throws IdInvalidException{
        //check id
        Subscriber subsDB = this.subscriberService.findById(subsRequest.getId());
        if(subsDB == null){
            throw new IdInvalidException("Subsriber id = "+subsRequest.getId()+" không tồn tại");
        }
        return ResponseEntity.ok().body(this.subscriberService.update(subsDB, subsRequest));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skills")
    public ResponseEntity<Subscriber> getSkill(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? 
        SecurityUtil.getCurrentUserLogin().get() : null;

        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }

}
