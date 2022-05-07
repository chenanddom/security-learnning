package com.itdom.securitylearnning.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //设置状态响应码
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer = response.getWriter();
        response.setHeader("Content-Type","application/json;charset=utf-8");
        writer.write("{\"status\":\"error\",\"msg\":\"权限不足,请联系管理员\"}");
        writer.flush();
        writer.close();
    }
}
