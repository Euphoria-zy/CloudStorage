package com.tcb.cloudstorage.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/*
 * code for class UserFile
 * @param null
 * 用户文件
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2023/1/11 20:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFile
{
    //文件id
    @TableId(value = "file_id")
    private int fileId;

    //文件id
    private String fileName;

    //文件类型（1-文档doc, 2-图片image,  3-视频video，4-音乐music, 5-其他other）
    private int fileType;

    //父文件夹id
    private int parentFolderId;

    //文件仓库id
    private int fileStoreId;

    //文件路径
    private String filePath;

    //文件下载次数
    private int downloadCount;

    //文件上传时间
    private Date uploadTime;

    //文件大小（单位kb）
    private int fileSize;

    //文件后缀
    private String postfix;
}
