package com.itdom.securitylearnning.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface CustomerService {
public boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
