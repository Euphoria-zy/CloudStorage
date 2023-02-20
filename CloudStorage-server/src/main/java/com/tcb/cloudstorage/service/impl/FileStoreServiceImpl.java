package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.FileStore;
import com.tcb.cloudstorage.domain.FileStoreInfo;
import com.tcb.cloudstorage.mapper.FileMapper;
import com.tcb.cloudstorage.mapper.FileStoreMapper;
import com.tcb.cloudstorage.mapper.FolderMapper;
import com.tcb.cloudstorage.service.FileService;
import com.tcb.cloudstorage.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileStoreServiceImpl extends ServiceImpl<FileStoreMapper, FileStore> implements FileStoreService
{
    @Autowired
    private FileStoreMapper fileStoreMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FolderMapper folderMapper;

    /**
     * @Description 创建文件仓库，用户注册时调用
     * @param fileStore
     * @return
     */
    @Override
    public int addFileStore(FileStore fileStore)
    {
        return fileStoreMapper.addFileStore(fileStore);
    }

    /**
     * @Description 根据id查找文件仓库
     * @param fileStoreId
     * @return
     */
    @Override
    public FileStore getFileStoreById(int fileStoreId)
    {
        return fileStoreMapper.selectById(fileStoreId);
    }

    /**
     * @Description 根据用户id查找文件仓库
     * @param userId
     * @return
     */
    @Override
    public FileStore getFileStoreByUserId(int userId)
    {
        LambdaQueryWrapper<FileStore> lambdaQueryWrapper = new QueryWrapper<FileStore>().lambda();
        lambdaQueryWrapper.eq(FileStore::getUserId, userId);
        return fileStoreMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * @Description 增加仓库可用容量
     * @param fileStoreId
     * @param size
     * @return
     */
    @Override
    public int addFileStoreSize(int fileStoreId, long size)
    {
        FileStore fileStore = new FileStore();
        fileStore.setFileStoreId(fileStoreId);
        fileStore.setCurrentSize(size);
        return fileStoreMapper.updateFileStoreById(fileStore);
    }

    /**
     * @Description 减少仓库可用容量
     * @param fileStoreId
     * @param size
     * @return
     */
    @Override
    public int subFileStoreSize(int fileStoreId, long size)
    {
        FileStore fileStore = new FileStore();
        fileStore.setFileStoreId(fileStoreId);
        fileStore.setCurrentSize(size);
        return fileStoreMapper.updateFileStoreById(fileStore);
    }

    /**
     * @Description 删除文件仓库
     * @param fileStoreId
     * @return
     */
    @Override
    public int delFileStoreById(int fileStoreId)
    {
        return fileStoreMapper.deleteById(fileStoreId);
    }

    @Override
    public FileStoreInfo getFileStoreInfo(int fileStoreId)
    {
        FileStoreInfo fileStoreInfo = fileMapper.getFileStoreInfoOfFile(fileStoreId);
        fileStoreInfo.setFolderCount(folderMapper.getFileStoreInfoOfFolder(fileStoreId));
        return fileStoreInfo;
    }
}
