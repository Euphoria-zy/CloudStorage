package com.tcb.cloudstorage.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/*
 * code for class Folder
 * @param null
 * 文件夹实体类
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2023/1/11 20:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Folder
{
    //文件夹id
    @TableId(value = "folder_id")
    private int folderId;

    //文件名
    private String folderName;

    //父文件夹id
    private int parentFolderId;

    //文件仓库id
    private int fileStoreId;

    //文件路径
    private String folderPath;

    //创建时间
    private Date createTime;


}
