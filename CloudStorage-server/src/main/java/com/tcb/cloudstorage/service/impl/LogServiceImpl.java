package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.UserLog;
import com.tcb.cloudstorage.mapper.LogMapper;
import com.tcb.cloudstorage.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, UserLog> implements LogService {

    @Autowired
    private LogMapper logMapper;

    private static final String[] typeNames={
            "上传",
            "下载",
            "新建文件夹",
            "移动",
            "复制",
            "重命名",
            "删除"
    };

    @Override
    public boolean recordLog(UserLog log)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateStr = null;
        try {
            dateStr = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.setRecordTime(new Timestamp(dateStr.getTime()));
        return logMapper.insertLog(log)>0;
    }

    @Override
    public List<UserLog> readLog(int userId) {
        List<UserLog> logs = logMapper.getLogs(userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UserLog log : logs) {
            log.setFormatTime(simpleDateFormat.format(new Date(log.getRecordTime().getTime())));
            log.setRecordTime(null);
            log.setOperationName(typeNames[log.getOperationType() - 1]);
        }
        return logs;
    }

    @Override
    public boolean deleteLog(int logId){
        System.out.println(logId);
        return logMapper.deleteLog(logId)>0;
    }
}
