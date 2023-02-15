package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.UserLog;
import com.tcb.cloudstorage.mapper.LogMapper;
import com.tcb.cloudstorage.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void recordLog(UserLog log) {
        logMapper.insertLog(log);
    }

    @Override
    public List<UserLog> readLog(int userId) {
        List<UserLog> logs = logMapper.getLogs(userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UserLog log : logs) {
            log.setFormatTime(simpleDateFormat.format(new Date(log.getRecordTime().getTime())));
            log.setOperationName(typeNames[log.getOperationType() - 1]);
            if(log.isFile()){
                log.setFileFolderName(log.getFileName());
            }else{
                log.setFileFolderName(log.getFolderName());
            }
        }
        return logs;
    }

}
