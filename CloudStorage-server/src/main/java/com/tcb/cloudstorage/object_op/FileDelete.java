package com.tcb.cloudstorage.object_op;

import com.qcloud.cos.COSClient;

public class FileDelete
{
    private COSClient cosClient;
    public FileDelete(COSClient cosClient)
    {
        this.cosClient = cosClient;
    }
    public void fileDelete(String BucketName, String AppId, String key)
    {
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;
        // 指定被删除的文件在 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示删除位于 folder 路径下的文件 picture.jpg
        cosClient.deleteObject(bucketName, key);
    }
}
