package com.itdom.oauth2.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.itdom.oauth2.mapper.SysUserMapper;
import com.itdom.oauth2.pojo.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().lambda();
        queryWrapper.eq(SysUser::getUsername,username);
        queryWrapper.eq(SysUser::getDeleted,0);
        queryWrapper.eq(SysUser::getStatus,1);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sysUsers)){
            return null;
        }
        SysUser sysUser = sysUsers.get(0);
//       log.info("对比结果:{}", passwordEncoder.matches(sysUser.getPassword(),passwordEncoder.encode("test123")));
        return new User(sysUser.getUsername(),sysUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normol"));
//        return new User(sysUser.getUsername().trim(),sysUser.getPassword().trim(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normol"));
    }
}
