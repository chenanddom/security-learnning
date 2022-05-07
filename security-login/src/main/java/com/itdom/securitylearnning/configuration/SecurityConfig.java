package com.itdom.securitylearnning.configuration;

import com.itdom.securitylearnning.handler.CustomerAccessDeniedHandler;
import com.itdom.securitylearnning.handler.FailureHandler;
import com.itdom.securitylearnning.service.UserServiveDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository(DataSource dataSource){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //第一启动需要创建表，第二次需要注释掉
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
    @Autowired
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;
    @Autowired
    private UserServiveDetailImpl userServiveDetail;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;



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
                .successForwardUrl("/toMain")
                //默认的ForwardAuthenticationSuccessHandler无法处理外部连接
//                .successForwardUrl("http://www.baidu.com")
//                ④⑤⑥⑦⑧⑨⑩
//                .successHandler(new SuccessHandler("http://www.baidu.com"))
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
                //如果添加了server.servlet.context-path=/security就需要在路径上添加对于的path放行才可以，否则失效
//               .mvcMatchers("/security/xxx").permitAll()
//              .antMatchers("/Home.html").hasAuthority("admin2")
                //校验访问需要某个具体角色的资源需要的角色
//              .antMatchers("/Home.html").hasRole("user2")
//              .antMatchers("/Home.html").hasAnyRole("user2","user")
                //此处只允许127.0.0.1的IP地址访问
//              .antMatchers("/Home.html").hasIpAddress("127.0.0.1")

            //所有请求都需要认证
            .anyRequest()
            .authenticated();
          //次方法相当于上面aurhenticated()
//        .access("@customerServiceImpl.hasPermission(request,authentication)");
        //防伪标识关闭
//        ①
        http.csrf().disable();
//       设置异常处理的hanle
        http.exceptionHandling().accessDeniedHandler(customerAccessDeniedHandler);

        http.rememberMe()
                //登录逻辑
                .userDetailsService(userServiveDetail)
                //持久层对象
                .tokenRepository(persistentTokenRepository);

        //退出登录指定跳转的页面，默认情况会带一个参数，如果不想要就需要做如下的设置
        http.logout()
                .logoutSuccessUrl("/login.html");



    }
}
