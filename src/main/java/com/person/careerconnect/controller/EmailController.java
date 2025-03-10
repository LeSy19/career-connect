package com.person.careerconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.careerconnect.service.EmailService;
import com.person.careerconnect.service.SubscriberService;
import com.person.careerconnect.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService,
    SubscriberService subscriberService){
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }
    @GetMapping
    @ApiMessage("Send simple email")
    public String sendSimpleEmail(){
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("sydev191@gmail.com", "Test1", "<h1> <b> hello </b> </h1>", false, true);
        this.subscriberService.sendSubscribersEmailJobs();
        return "Hello world";
    }
    
}
