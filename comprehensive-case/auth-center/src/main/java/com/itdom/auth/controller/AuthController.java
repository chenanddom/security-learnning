package com.itdom.auth.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.itdom.commen.ReturnResult;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class AuthController {
    private TokenEndpoint tokenEndpoint;

    @RequestMapping(value = "/token")
    public ReturnResult postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> responseEntity = tokenEndpoint.postAccessToken(principal, parameters);
        if (responseEntity.getStatusCode().value()!= HttpStatus.OK.value()){
            return ReturnResult.failed("获取token失败，请重新获取!!");
        }
        return ReturnResult.success(responseEntity.getBody());
    }
}
