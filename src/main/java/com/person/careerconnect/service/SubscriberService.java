package com.person.careerconnect.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.person.careerconnect.domain.Job;
import com.person.careerconnect.domain.Skill;
import com.person.careerconnect.domain.Subscriber;
import com.person.careerconnect.domain.response.email.ResEmailJob;
import com.person.careerconnect.repository.JobRepository;
import com.person.careerconnect.repository.SkillRepository;
import com.person.careerconnect.repository.SubscriberRepository;
@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository,
            SkillRepository skillRepository,
            JobRepository jobRepository,
            EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean isExistsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber create(Subscriber subs) {
        // check skill
        if (subs.getSkills() != null) {
            List<Long> reqSkill = subs.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill);
            subs.setSkills(dbSkill);
        }

        return this.subscriberRepository.save(subs);

    }

    public Subscriber findById(long id) {
        Optional<Subscriber> subsOptional = this.subscriberRepository.findById(id);
        if (subsOptional != null) {
            return subsOptional.get();
        }
        return null;
    }

    public Subscriber update(Subscriber subsDB, Subscriber subsRequest) {
        // check skill
        if (subsRequest.getSkills() != null) {
            List<Long> reqSkill = subsRequest.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subsDB.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subsDB);
    }

    public ResEmailJob convertJobToSendEmail(Job job){
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(
            skill -> new ResEmailJob.SkillEmail(skill.getName()))
        .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    // @Scheduled(fixedRate = 1000)
    // public void testCron(){
    //     System.out.println("Test cron job");
    // }

    public void sendSubscribersEmailJobs() {
        //Lấy tất cả subscriber từ database
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        
        if (listSubs != null && listSubs.size() > 0) {
            //lặp qua từng subscriber
            for (Subscriber sub : listSubs) {
                //lấy danh sách skill của subscriber
                List<Skill> listSkills = sub.getSkills();
                //check subsriber có skill nào không
                if (listSkills != null && listSkills.size() > 0) {
                    //Lấy danh sách công việc theo skill của subscriber
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                        //Chuyển đổi danh sách job thành danh sách ResEmailJob để gửi email
                        List<ResEmailJob> arr = listJobs.stream().map(
                        job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                listJobs);
                    }
                }
            }
        }
    }

    public Subscriber findByEmail(String email){
        return this.subscriberRepository.findByEmail(email);
    }

}
