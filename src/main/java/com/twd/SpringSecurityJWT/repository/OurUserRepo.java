package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OurUserRepo extends JpaRepository<OurUsers, Integer> {
    
    Optional<OurUsers> findByUsername(String username);

    void deleteById(Long userId);

    boolean existsById(Long userId);

    Optional<OurUsers> findById(Long userId);
}
