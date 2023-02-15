package com.tcb.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcb.cloudstorage.domain.UserLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper  extends BaseMapper<UserLog> {
    int insertLog(UserLog log);
    List<UserLog> getLogs(int userId);
}
