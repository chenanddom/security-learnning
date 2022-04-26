package com.itdom.securitylearnning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiveDetailImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            如果查找不到就会抛出UsernameNotFoundException

        if (!"admin".equalsIgnoreCase(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        String encode = passwordEncoder.encode("test123");

        return new User(username,encode, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normol"));
    }
}
