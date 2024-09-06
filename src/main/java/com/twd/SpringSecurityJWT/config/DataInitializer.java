package com.twd.SpringSecurityJWT.config;

import com.twd.SpringSecurityJWT.entity.OurUsers;
import com.twd.SpringSecurityJWT.repository.OurUserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner init() {
        return (ApplicationArguments args) -> {
            if (ourUserRepo.findByUsername("admin123").isEmpty()) {
                OurUsers admin = new OurUsers();
                admin.setUsername("admin123");
                admin.setPassword(passwordEncoder.encode("admin@123"));
                admin.setRole("ADMIN");
                ourUserRepo.save(admin);
            }
        };
    }
    
}
