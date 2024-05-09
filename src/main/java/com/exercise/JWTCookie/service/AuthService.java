package com.exercise.JWTCookie.service;

import com.exercise.JWTCookie.dto.ReqResponse;
import com.exercise.JWTCookie.entity.User;
import com.exercise.JWTCookie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqResponse signUp(ReqResponse registrationRequest) {
        ReqResponse resp = new ReqResponse();
        try {
            Optional<User> existingUser = userRepository.existsByEmail(registrationRequest.getEmail());

            if (existingUser.isPresent()) {
                resp.setMessage("Registration failed, email already exists");
                resp.setStatusCode(409);
                return resp;
            } else {
                User user = new User();
                user.setEmail(registrationRequest.getEmail());
                user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                user.setRole(registrationRequest.getRole());

                User savedUser = userRepository.save(user);
                if (savedUser != null && savedUser.getId() > 0) {
                    resp.setOurUsers(savedUser);
                    resp.setMessage("User Saved Successfully");
                    resp.setStatusCode(200);
                } else {
                    resp.setMessage("Failed to save user");
                    resp.setStatusCode(500);
                }
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqResponse signIn(ReqResponse signinRequest){
        ReqResponse response = new ReqResponse();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
            response.setMessage("Login failed");
        }
        return response;
    }

}
