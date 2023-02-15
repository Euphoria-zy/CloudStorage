package com.tcb.cloudstorage.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//日志记录
public class UserLog {

    /* 构造示例：
    UserLog.builder()
            .userId(userId)
            .recordTime(new Timestamp(new Date().getTime()))
            .operationType(operationType)
            .fileFolderId(fileId||folderId)
        .build()*/

    @TableId(value = "log_id")
    private int logId;//数据库自增属性
    private int userId;//正常属性，【构造时传入】
    private Timestamp recordTime;//正常属性，【构造时传入】
    private String formatTime;//传向前段的临时属性
    private int operationType;//正常属性，【构造时传入】
/*    取值说明
    1-"上传"
    2-"下载"
    3-"新建文件夹"
    4-"移动"
    5-"复制"
    6-"重命名"
    7-"删除"*/
    private String operationName;//传向前段的临时属性
    private boolean isFile;//正常属性，【构造时传入】
/*    取值说明
    true-"文件"
    false-"文件夹"*/
    private String fileFolderName;//正常属性，【构造时传入】
    private boolean isOperationSuccess;//正常属性，【构造时传入】

}
