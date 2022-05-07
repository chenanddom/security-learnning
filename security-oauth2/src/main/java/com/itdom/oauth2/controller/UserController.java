package com.itdom.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("currentUser")
    public Object getUser(Authentication authentication) {
        return authentication.getPrincipal();
    }
}
