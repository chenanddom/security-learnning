package com.itdom.oauth2.configuration;

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
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}

@Bean
public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
}

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.formLogin()
//                .loginPage("/login")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**","/login","/logout/**").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and().csrf().disable().cors();

        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/oauth/**","/login/**","/logout/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll();
    }
}
