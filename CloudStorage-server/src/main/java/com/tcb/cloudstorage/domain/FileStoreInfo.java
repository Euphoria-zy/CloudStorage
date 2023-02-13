package com.tcb.cloudstorage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * code for class FileStoreInfo
 * @param null
 * 文件仓库信息
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2023/1/11 20:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileStoreInfo
{
    private FileStore fileStore;

    //1-文档类型数量
    private int doc;

    //2-图片数量
    private int image;

    //3-视频数量
    private int video;

    //4-音乐数量
    private int music;

    //5-其他数量
    private int other;

    //文件数量
    private int fileCount;

    //文件夹数量
    private int folderCount;

}
