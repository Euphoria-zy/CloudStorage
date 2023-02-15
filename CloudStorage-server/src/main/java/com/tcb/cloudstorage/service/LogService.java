package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.UserLog;

import java.text.ParseException;
import java.util.List;

public interface LogService {

    //每次操作文件完，都要调用本方法，将操作日志存进数据库
    boolean recordLog(UserLog log) throws ParseException;

    //从数据库中，读取出一个用户的操作日志
    List<UserLog> readLog(int userId);

    boolean deleteLog(int logId);
}
