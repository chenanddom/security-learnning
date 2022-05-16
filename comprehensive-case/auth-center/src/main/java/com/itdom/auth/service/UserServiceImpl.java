package com.itdom.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.itdom.auth.entity.SysUser;
import com.itdom.auth.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {


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
        return new User(sysUser.getUsername(),sysUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin,ROLE_normal,ROLE_user"));
    }
}
