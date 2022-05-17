# 集成springcloud gateway实现单点登录的
Oauth2架构图如下：
![SpringCloudGateway集成Oauth2的架构图](./files\网关继承Oauth认证鉴权的架构图.PNG)


为了实现springcloudgateway实现对oauth2的支持，需要提供以下几个服务：
1. Oauth2授权中心，实现对用户的登录的授权获取令牌。
2. 网关SpringCloudGateway,而且这个模块需要提供以下的依赖
    ```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-oauth2-resource-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
    ```
3. 下游的业务服务


## Oauth2授权中心
Oauth2授权中心同样也是需要在以上的几个依赖，除此之外我们还需要做一下的配置
一.security相关的配置
[SecurityConfiguration.java](./auth-center/src\main\java/com\itdom\auth\configuration\SecurityConfiguration.java)
```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭防伪验证
        http.csrf().disable()
        //请求需要授权
            .authorizeRequests()
        //放行下面路径的请求
            .antMatchers("/oauth/**","/login/**","/logout/**","/auth-center/oauth/**")
            .permitAll()
        //其他的所有请求都需要认证    
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll();
    }
```
二. jwt和security相关的配置
[JwtTokenStoreConfiguration](./auth-center/src/main/java/com\itdom\auth\configuration\JwtTokenStoreConfiguration.java)

三. 自定义ClientDetailService的service类
这个类主要就是从我们的数据库里面去获取我们每个请求获取access_token 的客户端的信息。
[ClientDetailsServiceImpl.java](./auth-center/src/main/java/com/itdom/auth/service/ClientDetailsServiceImpl.java)

四. 查找系统用户的用户信息
[UserServiceImpl.java](./auth-center/src/main/java/com/itdom/auth/service/UserServiceImpl.java)


五.授权服务器的定义
```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /*
        配置验证管理器
        这里主要是上面提到的这段代码生成的
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
        */
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                //配置token的存储方式
                .tokenStore(tokenStore)
                //设置oauth2的token和jwt的转换器
                .accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //客户端信息的查找使用自定义的
        clients.withClientDetails(clientDetailsService);

    }
    //oauth2必须要要重写的方法
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        //获取密钥需要身份认证，使用单点登录必须要配置
        security.tokenKeyAccess("isAuthenticated()");
    }
}
```

到此就基本可以实现授权服务器的开发了，但是如果还需要进行扩展，就需要自定AuthentionManager和相关的serviceDetail的查找


## Gateway的相关配置
一.令牌认证管理器
[JwtAuthenticationManager.java](./edge-server/src/main/java/com/itdom/edge/configuration/JwtAuthenticationManager.java)

二.鉴权管理器
[ResourceManager.java](./edge-server/src/main/java/com/itdom/edge/configuration/ResourceManager.java)
以上两个步骤都是需要的，当然可以考虑合在一起处理了。

三. WebFluxChain过滤器链的配置
需要将令牌认证管理器和鉴权管理器一起配置到过滤器链，实现对请求的token和权限的校验
除此之外还需要定义异常处理和无权限访问和token过期的处理器到过滤器链里面,如下代码:
```java
/**
     * 自定义未授权响应
     */
    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> ResponseUtils.writeErrorInfo(response, ResultCode.ACCESS_UNAUTHORIZED));
            return mono;
        };
    }

    /**
     * token无效或者已过期自定义响应
     */
    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, e) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> ResponseUtils.writeErrorInfo(response, ResultCode.TOKEN_INVALID_OR_EXPIRED));
            return mono;
        };
    }
```
```java
    @Bean
    public SecurityWebFilterChain webFluxFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(tokenAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        http.httpBasic()
                .disable()
                .cors()
                .disable()
                .authorizeExchange()
                /*
                白名单放行
                 */
//                .pathMatchers(ArrayUtil.toArray(systemParameter.getIgnoreUrls(), String.class))
                .pathMatchers("/oauth/**","/login/**","/auth-center/**")
                .permitAll()
                //其他的请求都需要认证
                .anyExchange()
                .access(accessManager)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and().addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
```
[认证、鉴权、异常、无权访问，过期的处理的配置SecurityConfiguration.java](./edge-server/src/main/java/com/itdom/edge/configuration/SecurityConfiguration.java)