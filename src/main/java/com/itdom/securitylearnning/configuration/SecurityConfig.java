package com.itdom.securitylearnning.configuration;

import com.itdom.securitylearnning.handler.FailureHandler;
import com.itdom.securitylearnning.handler.SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单提交,
//        ①
        http.formLogin()
                //自定义的用户名和密码，设计到org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
                //若要自定义用户名和密码的参数可以使用下面的两个方法实现
//                ③
//                .usernameParameter()
//                .passwordParameter()
//                ②
                //当拦截到url为/user/login的表单请求的时候就走登录请求
                .loginProcessingUrl("/login")
                //登录成功后跳转页面，必须是post请求
//                .successForwardUrl("/toMain")
                //默认的ForwardAuthenticationSuccessHandler无法处理外部连接
//                .successForwardUrl("http://www.baidu.com")
//                ④⑤⑥⑦⑧⑨⑩
                .successHandler(new SuccessHandler("http://www.baidu.com"))
//                .failureForwardUrl("/toError")
                .failureHandler(new FailureHandler("/error.html"))
                //自定义登录表单
                .loginPage("/login.html");

//        ①
        http.authorizeRequests()
             //对某些url放行
            .antMatchers("/login.html","/error.html")
            .permitAll()
//                放行静态资源
//            .antMatchers("/images/**").permitAll()
            //所有请求都需要认证
            .anyRequest()
            .authenticated();
        //防伪标识关闭
//        ①
        http.csrf().disable();
    }
}
