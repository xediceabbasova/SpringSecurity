package com.company.springsecurity.jwttoken.repository;


import com.company.springsecurity.jwttoken.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
