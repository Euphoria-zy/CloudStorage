package com.tcb.cloudstorage.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * code for class FileStore
 * @param null
 * 文件仓库类
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2023/1/11 20:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileStore
{
    //文件仓库id
    @TableId(value = "file_store_id")
    private int fileStoreId;

    //用户id，一个用户一个文件仓库
    private int userId;

    //存储空间大小
    private int currentSize;

    //总容量
    private int maxSize;

}
