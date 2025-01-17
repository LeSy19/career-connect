package com.person.careerconnect.repository;

import com.person.careerconnect.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
