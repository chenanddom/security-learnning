package com.itdom.securitylearnning.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        //获取访问服务的主体
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails){
            UserDetails userDetails = (UserDetails) principal;
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return authorities.contains(new SimpleGrantedAuthority(request.getRequestURI()));
        }

        return false;
    }
}
