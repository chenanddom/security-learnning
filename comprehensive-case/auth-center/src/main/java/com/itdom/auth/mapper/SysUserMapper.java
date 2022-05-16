package com.itdom.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itdom.auth.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户持久层
 *
 * @author haoxr
 * @date 2022/1/14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
