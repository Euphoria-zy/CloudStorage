package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.Folder;
import com.tcb.cloudstorage.domain.UserFile;

import java.util.List;

public interface FolderService
{
    /**
     * @Description 创建文件夹
     * @param folder
     * @return
     */
    public int addFolder(Folder folder);

    /**
     * @Description 更新文件夹
     * @param folder
     * @return
     */
    public boolean updateFolder(Folder folder);

    /**
     * @Description 根据id删除文件夹
     * @param folderId
     * @return
     */
    public int deleteFolder(int folderId);

    /**
     * @Description 根据id获取文件夹
     * @param folderId
     * @return
     */
    public Folder getFolderById(int folderId);

    /**
     * @Description 根据父文件夹id获取下面的文件夹
     * @param parentFolderId
     * @return
     */
    public List<Folder> getFolderByParentFolderId(int parentFolderId);

    /**
     * @Description 根据文件仓库id获取根目录下面的文件夹
     * @param fileStoreId
     * @return
     */
    public List<Folder> getRootFolderByFileStoreId(int fileStoreId);

    /**
     * @Description 添加文件夹并返回folderId
     * @param folder
     * @return 是否添加成功
     */
    public int addFolderReturnFolderId(Folder folder);

    /**
     * @Description 根据判断父文件夹下是否已经存在同名文件夹
     * @param parentFolderId
     * @param folderName
     * @return
     */
    public Folder getFolderByPIdAndName(int parentFolderId, String folderName);


}
