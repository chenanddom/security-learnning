package com.itdom.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//实例化密码加密编码器
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}
/*
生成认证管理器,此处要提供一个，因为自定义UserDetailService之后就会无法注入默认的AuthenticationManager了，所以需要手动生成一个
 */
@Bean
@Override
public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/oauth/**","/login/**","/logout/**","/auth-center/oauth/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll();
    }
}
