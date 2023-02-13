package com.tcb.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcb.cloudstorage.domain.Folder;
import com.tcb.cloudstorage.domain.UserFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder>
{

    /**
     * @Description 根据父文件夹id获取下面的文件夹
     * @param parentFolderId
     * @return
     */
    public List<Folder> getFolderByParentFolderId(int parentFolderId);

    /**
     * @Description 根据文件仓库id获取根目录的文件夹
     * @param fileStoreId
     * @return
     */
    public List<Folder> getRootFolderByFileStoreId(int fileStoreId);

    public int getFileStoreInfoOfFolder(int filStoreId);

    /**
     * @Description 添加文件夹并返回folderId
     * @param folder
     * @return folderId
     */
    public int addFolderReturnId(Folder folder);

}
