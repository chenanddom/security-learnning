package com.itdom.securitylearnning.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SuccessHandler implements AuthenticationSuccessHandler {
    private String url;

    public SuccessHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       //自定义跳转的url，默认的跳转指定在本地服务内，如果要跳转到外部的服务，就需要进行自定义的Handler,继承AuthenticationSuccessHandler
        User user = (User) authentication.getPrincipal();
//        处于安全的考虑password会被打印为null
        log.info("获取信息-username:{},password:{},authorities:{}",user.getUsername(),user.getPassword(),user.getAuthorities());
        response.sendRedirect(this.url);
    }
}
