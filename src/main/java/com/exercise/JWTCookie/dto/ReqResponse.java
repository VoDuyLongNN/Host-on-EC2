package com.exercise.JWTCookie.dto;

import com.exercise.JWTCookie.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqResponse {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String expirationTime;
    private String name;
    private String email;
    private String role;
    private String password;
    private User ourUsers;
}
