package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.FileStoreInfo;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.domain.UserFile;

import java.util.List;

public interface FileService
{


    /**
     * @Description 添加文件元数据
     * @param userFile
     * @return
     */
    public boolean addUserFile(UserFile userFile);


    /**
     * @Description 根据id更新文件信息
     * @param userFile
     * @return
     */
    public boolean updateUserFileById(UserFile userFile);

    /**
     * @Description 根据文件夹id获取文件夹下的文件
     * @param folderId
     * @return
     */
    public List<UserFile> getUserFileByParentFolderId(int folderId);

    /**
     * @Description 根据文件仓库id获取根目录下的文件
     * @param fileStoreId
     * @return
     */
    public List<UserFile> getRootFileByFileStoreId(int fileStoreId);

    public UserFile getFileByFileId(int fileId);

    public boolean deleteFileById(int fileId);

    //获取文件类型
    public int getFileTypeByPostfix(String type);

    //检查文件名是否合法
    public boolean checkFileName(String target);

    /**
     * @Description 根据判断父文件夹下是否已经存在同名文件
     * @param parentFolderId
     * @param fileName
     * @param postfix
     * @return
     */
    public boolean isFileRepeat(int parentFolderId, String fileName, String postfix);

    /**
     * @Description 获取文件图标url，图片文件返回图片地址，视频文件返回截帧图片地址
     * @param userFile
     * @return
     */
    public String getFileImage(UserFile userFile);

    /**
     * @Description 根据文档类型后缀，获取图标
     * @param postfix
     * @return
     */
    public String getDocumentFileImage(String postfix);

    /**
     * @Description 获取视频截帧图片url
     * @param userFile
     * @return
     */
    public String getVideoImage(UserFile userFile);

}
