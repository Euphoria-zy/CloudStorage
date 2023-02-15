package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.UserLog;

import java.util.List;

public interface LogService {

    //每次操作文件完，都要调用本方法，将操作日志存进数据库
    void recordLog(UserLog log);

    //从数据库中，读取出一个用户的操作日志
    List<UserLog> readLog(int userId);
}
