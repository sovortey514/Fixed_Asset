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
import org.springframework.security.core.Authentication;
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
            if (authentication == null || !authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
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
            ourUsers.setName(registrationRequest.getName());
            ourUsers.setUsername(registrationRequest.getUsername());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setEnabled(true);

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

    public List<OurUsers> getAllUsers() {
        return ourUserRepo.findAll();
    }

    @Transactional
    public ReqRes deleteUser(Long userId) {
        ReqRes response = new ReqRes();
        try {
            if (!ourUserRepo.existsById(userId)) {
                response.setMessage("User not found.");
                response.setStatusCode(404);
                return response;
            }

            ourUserRepo.deleteById(userId);
            response.setMessage("User deleted successfully.");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Delete error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
    
            // Retrieve the user from the repository
            OurUsers user = ourUserRepo.findByUsername(signinRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
    
            // Check if the user account is enabled
            if (!user.isEnabled()) {
                response.setStatusCode(403);
                response.setError("User account is disabled.");
                return response;
            }
    
            // Generate JWT and refresh token
            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
    
            // Set the successful response
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully signed in.");
        } catch (BadCredentialsException e) {
            response.setStatusCode(401);
            response.setError("Invalid username or password.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Sign-in error: " + e.getMessage());
        }
        return response;
    }
    

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String username = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers user = ourUserRepo.findByUsername(username)
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

    @Transactional
    public ReqRes updateUser(Long userId, RegisterRequest updateRequest) {
        ReqRes response = new ReqRes();
        try {
            // Retrieve user by ID, throw exception if not found
            OurUsers user = ourUserRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (updateRequest.getName() != null) {
                user.setName(updateRequest.getName());
            }
            if (updateRequest.getUsername() != null) {
                user.setUsername(updateRequest.getUsername());
            }
            if (updateRequest.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
            }
            if (updateRequest.getRole() != null) {
                user.setRole(updateRequest.getRole());
            }

            // Save the updated user to the database
            OurUsers updatedUser = ourUserRepo.save(user);
            response.setOurUsers(updatedUser);
            response.setMessage("User updated successfully.");
            response.setStatusCode(200);
        } catch (Exception e) {
            // Handle any exceptions that occur during the update process
            response.setStatusCode(500);
            response.setError("Update error: " + e.getMessage());
        }
        return response;
    }

    public OurUsers getUserById(Long userId) {
        return ourUserRepo.findById(userId).orElse(null);
    }

    @Transactional
    public ReqRes disableUser(Long userId) {
        ReqRes response = new ReqRes();
        try {
            // Ensure the admin cannot disable themselves
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OurUsers currentAdmin = (OurUsers) authentication.getPrincipal();

            if (currentAdmin.getId().equals(userId)) {
                response.setMessage("Admin cannot disable their own account.");
                response.setStatusCode(403);
                return response;
            }

            OurUsers user = ourUserRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getUsername().equals("admin123")) {
                response.setMessage("Cannot disable the default admin account.");
                response.setStatusCode(403);
                return response;
            }

            user.setEnabled(false); // Disable the user
            ourUserRepo.save(user);
            response.setMessage("User disabled successfully.");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Disable error: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public ReqRes enableUser(Long userId) {
        ReqRes response = new ReqRes();
        try {
            // Ensure the admin cannot enable themselves
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OurUsers currentAdmin = (OurUsers) authentication.getPrincipal();

            if (currentAdmin.getId().equals(userId)) {
                response.setMessage("Admin cannot enable their own account.");
                response.setStatusCode(403);
                return response;
            }

            OurUsers user = ourUserRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getUsername().equals("admin123")) {
                response.setMessage("Cannot enable the default admin account.");
                response.setStatusCode(403);
                return response;
            }

            user.setEnabled(true); // Enable the user
            ourUserRepo.save(user);
            response.setMessage("User enabled successfully.");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Enable error: " + e.getMessage());
        }
        return response;
    }
}
