package com.itdom.oauth2.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class AuthController {
    private TokenEndpoint tokenEndpoint;

    @RequestMapping(value = "/token")
    public Map postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        if (CollectionUtils.isNotEmpty(parameters)) {
            parameters.put("client_id", "server-edge");
            parameters.put("client_secrect", "test123");
            parameters.put("grant_type", "password");
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("code", 0);
        result.put("messages", "request success");
        result.put("access_token", tokenEndpoint.postAccessToken(principal, parameters).getBody());
        return result;
    }
}
