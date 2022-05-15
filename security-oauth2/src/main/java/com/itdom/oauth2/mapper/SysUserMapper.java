package com.itdom.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itdom.oauth2.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户持久层
 *
 * @author haoxr
 * @date 2022/1/14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
