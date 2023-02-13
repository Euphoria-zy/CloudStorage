package com.tcb.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcb.cloudstorage.domain.FileStoreInfo;
import com.tcb.cloudstorage.domain.UserFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<UserFile>
{

    /**
     * @Description 根据文件夹id获取文件夹下的文件
     * @param folderId
     * @return
     */
    public List<UserFile> getUserFileByFolderId(int folderId);

    /**
     * @Description 根据文件仓库id获取根目录下的文件
     * @param fileStoreId
     * @return
     */
    public List<UserFile> getRootFileByFileStoreId(int fileStoreId);

    public FileStoreInfo getFileStoreInfoOfFile(int fileStoreId);
}
