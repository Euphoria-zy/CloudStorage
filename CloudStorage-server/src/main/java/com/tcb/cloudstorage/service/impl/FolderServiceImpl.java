package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.Folder;
import com.tcb.cloudstorage.domain.UserFile;
import com.tcb.cloudstorage.mapper.FolderMapper;
import com.tcb.cloudstorage.service.FileService;
import com.tcb.cloudstorage.service.FileStoreService;
import com.tcb.cloudstorage.service.FolderService;
import com.tcb.cloudstorage.utils.COSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService
{

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private FileService fileService;
    @Autowired
    private FileStoreService fileStoreService;

    /**
     * @Description 创建文件夹
     * @param folder
     * @return
     */
    @Override
    public int addFolder(Folder folder)
    {
        return folderMapper.insert(folder);
    }

    /**
     * @Description 更新文件夹信息
     * @param folder
     * @return
     */
    @Override
    public boolean updateFolder(Folder folder)
    {
        return folderMapper.updateById(folder)>0;
    }

    /**
     * @Description 根据id删除文件夹
     * @param folderId
     * @return
     */
    @Override
    public int deleteFolder(int folderId)
    {
        return folderMapper.deleteById(folderId);
    }

    /**
     * @Description 根据文件夹id获取文件夹
     * @param folderId
     * @return
     */
    @Override
    public Folder getFolderById(int folderId)
    {
        return folderMapper.selectById(folderId);
    }

    /**
     * @Description 根据父文件夹id获取下面的文件夹
     * @param parentFolderId
     * @return
     */
    @Override
    public List<Folder> getFolderByParentFolderId(int parentFolderId)
    {
        return folderMapper.getFolderByParentFolderId(parentFolderId);
    }

    @Override
    public List<Folder> getRootFolderByFileStoreId(int fileStoreId)
    {
        return folderMapper.getRootFolderByFileStoreId(fileStoreId);
    }

    /**
     * @Description 添加文件夹并返回folderId
     * @param folder
     * @return 是否添加成功
     */
    @Override
    public int addFolderReturnFolderId(Folder folder)
    {
        return folderMapper.addFolderReturnId(folder);
    }

    @Override
    public Folder getFolderByPIdAndName(int parentFolderId, String folderName)
    {
        LambdaQueryWrapper<Folder> lambdaQueryWrapper = new QueryWrapper<Folder>().lambda();
        lambdaQueryWrapper.eq(Folder::getParentFolderId, parentFolderId);
        lambdaQueryWrapper.eq(Folder::getFolderName, folderName);
        return folderMapper.selectOne(lambdaQueryWrapper);
    }

}
