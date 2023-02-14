package com.tcb.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcb.cloudstorage.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    public int insertUser(User user);
}
