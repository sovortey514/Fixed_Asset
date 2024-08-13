package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.OurUsers;
import com.twd.SpringSecurityJWT.entity.RegisterRequest;
import com.twd.SpringSecurityJWT.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final List<String> VALID_ROLES = List.of("USER", "ADMIN");

    @Transactional
    public ReqRes signUp(RegisterRequest registrationRequest) {
        ReqRes response = new ReqRes();
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
                response.setMessage("Only admins can register new users.");
                response.setStatusCode(403);
                return response;
            }

            if (!VALID_ROLES.contains(registrationRequest.getRole())) {
                response.setMessage("Invalid role specified.");
                response.setStatusCode(400);
                return response;
            }

            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());

            OurUsers savedUser = ourUserRepo.save(ourUsers);
            if (savedUser != null && savedUser.getId() > 0) {
                response.setOurUsers(savedUser);
                response.setMessage("User registered successfully.");
                response.setStatusCode(200);
            } else {
                response.setMessage("Failed to register user.");
                response.setStatusCode(500);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Registration error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
    
            OurUsers user = ourUserRepo.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
    
            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
    
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully signed in.");
        } catch (BadCredentialsException e) {
            response.setStatusCode(401);
            response.setError("Invalid email or password.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Sign-in error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers user = ourUserRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                String newJwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(newJwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Token refreshed successfully.");
            } else {
                response.setStatusCode(401);
                response.setError("Invalid or expired token.");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Token refresh error: " + e.getMessage());
        }
        return response;
    }
}
