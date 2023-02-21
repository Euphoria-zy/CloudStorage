package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.FileStore;
import com.tcb.cloudstorage.domain.FileStoreInfo;
import org.springframework.stereotype.Repository;


public interface FileStoreService
{

    //创建文件仓库
    public int addFileStore(FileStore fileStore);

    //根据id查找文件仓库
    public FileStore getFileStoreById(int fileStoreId);

    //根据用户id查找文件仓库
    public FileStore getFileStoreByUserId(int userId);

    //增加仓库可用容量
    public int addFileStoreSize(int fileStoreId, long size);

    //减少仓库可用容量
    public int subFileStoreSize(int fileStoreId, long size);

    //删除文件仓库
    public int delFileStoreById(int fileStoreId);

    //获取文件仓库信息
    public FileStoreInfo getFileStoreInfo(int fileStoreId);

}
