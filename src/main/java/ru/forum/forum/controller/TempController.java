package ru.forum.forum.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TempController {
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        return "ID: %s ".formatted(request.getSession().getId());
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
