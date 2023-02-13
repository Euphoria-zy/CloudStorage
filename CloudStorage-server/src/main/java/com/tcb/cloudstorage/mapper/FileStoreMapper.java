package com.tcb.cloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcb.cloudstorage.domain.FileStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileStoreMapper extends BaseMapper<FileStore>
{

    /**
     * @Description 创建文件仓库
     * @param fileStore
     * @return
     */
    public int addFileStore(FileStore fileStore);

    public int updateFileStoreById(FileStore fileStore);

}
