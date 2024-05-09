package com.exercise.JWTCookie.controller;

import com.exercise.JWTCookie.dto.ReqResponse;
import com.exercise.JWTCookie.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signUp(@ModelAttribute ReqResponse signUpRequest, RedirectAttributes redirectAttributes) {
        ReqResponse response = authService.signUp(signUpRequest);
        String message = response.getMessage();
        redirectAttributes.addFlashAttribute("alertMessageRegister", message);
        return "redirect:/public/register";
    }

    @PostMapping("/signin")
    public String signIn(@ModelAttribute ReqResponse signInRequest, HttpServletResponse httpResponse, RedirectAttributes redirectAttributes) {
        ReqResponse response = authService.signIn(signInRequest);

        if (response.getStatusCode() == 200 && response.getToken() != null) {

            Cookie tokenCookie = new Cookie("token", response.getToken());

            tokenCookie.setMaxAge(3600);

            tokenCookie.setPath("/");

            httpResponse.addCookie(tokenCookie);

            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("error", "login failed. Please check your credentials");
            return "redirect:/public/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        SecurityContextHolder.clearContext();
        session.invalidate();

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/public/login";
    }

}
