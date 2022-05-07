package com.itdom.oauth2.configuration;

//import com.itdom.oauth2.service.ClientDetailsServiceImpl;
import com.itdom.oauth2.service.ClientDetailsServiceImpl;
import com.itdom.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                //配置token的存储方式
                .tokenStore(tokenStore)
                //设置oauth2的token和jwt的转换器
                .accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
/*        clients.inMemory()
                //配置的client-id
                .withClient("server-edge")
                //配置的client-secrect
                .secret(passwordEncoder.encode("test123"))
                //配置访问token的有效时长
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(7200)
                //跳转的url
//                .redirectUris("http://www.baidu.com")
                .redirectUris("http://localhost:8090/login")
                .autoApprove(true)
                //配置申请权限的范围
                .scopes("all")
                //指定的授权莫斯可以是"implicit","refresh_token", "password", "authorization_code"
//                .authorizedGrantTypes("authorization_code") ;
                .authorizedGrantTypes("password","refresh_token","authorization_code") ;*/
        //将client的配置从数据库中提取
        clients.withClientDetails(clientDetailsService);

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        //获取密钥需要身份认证，使用单点登录必须要配置
        security.tokenKeyAccess("isAuthenticated()");
    }
}
