package com.itdom.oauth2.service;

import com.itdom.oauth2.mapper.SysOauthClientMapper;
import com.itdom.oauth2.pojo.entity.SysOauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Autowired
    private SysOauthClientMapper sysOauthClientMapper;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        SysOauthClient client = sysOauthClientMapper.selectById(clientId);
        if (client==null){
            throw new RuntimeException("No such client!!!");
        }
        BaseClientDetails clientDetails = new BaseClientDetails(
                client.getClientId(),
                client.getResourceIds(),
                client.getScope(),
                client.getAuthorizedGrantTypes(),
                client.getAuthorities(),
                client.getWebServerRedirectUri()
        );
        clientDetails.setClientSecret(client.getClientSecret());
        clientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(client.getAutoapprove()));
        clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
        return clientDetails;
    }
}
