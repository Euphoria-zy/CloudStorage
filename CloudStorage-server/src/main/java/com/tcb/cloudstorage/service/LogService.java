package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.UserLog;

import java.text.ParseException;
import java.util.List;

public interface LogService {

    /**
     * @Description 插入一条log
     * @param log
     * @return
     */
    //每次操作文件完，都要调用本方法，将操作日志存进数据库
    boolean recordLog(UserLog log) throws ParseException;

    /**
     * @Description 返回userId的所有log
     * @param userId
     * @return
     */
    //从数据库中，读取出一个用户的操作日志
    List<UserLog> readLog(int userId);

    /**
     * @Description 删除一条log
     * @param logId
     * @return
     */
    boolean deleteLog(int logId);
}
