package com.twd.SpringSecurityJWT.controller;

import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.OurUsers;
import com.twd.SpringSecurityJWT.entity.RegisterRequest;
import com.twd.SpringSecurityJWT.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/auth")
// @CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReqRes> signUp(@RequestBody RegisterRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest) {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<OurUsers>> getAllUsers() {
        List<OurUsers> users = authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Long userId) {
        ReqRes response = authService.deleteUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest updateRequest) {
        ReqRes response = authService.updateUser(userId, updateRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<OurUsers> getUserById(@PathVariable Long userId) {
        OurUsers user = authService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{userId}/disable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReqRes> disableUser(@PathVariable Long userId) {
        ReqRes response = authService.disableUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/users/{userId}/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReqRes> enableUser(@PathVariable Long userId) {
        ReqRes response = authService.enableUser(userId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<OurUsers> getUserByUsername(@PathVariable String username) {
        Optional<OurUsers> userOptional = authService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
