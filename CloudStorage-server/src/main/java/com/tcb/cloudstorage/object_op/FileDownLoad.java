package com.tcb.cloudstorage.object_op;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;

import java.io.File;
import java.io.IOException;

public class FileDownLoad
{
    private COSClient cosClient;
    public FileDownLoad(COSClient cosClient)
    {
        this.cosClient = cosClient;
    }
    public void download(String BucketName, String AppId, String key, String localPath)
    {
        // Bucket 的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;
        // 指定文件在 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示下载的文件 picture.jpg 在 folder 路径下
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        try
        {
            // 方法1 获取下载输入流
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
            // 下载对象的 CRC64
            String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();
            System.out.println(crc64Ecma);
            // 关闭输入流
            cosObjectInput.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        // 方法2 下载文件到本地的路径，例如 D 盘的某个目录
        String outputFilePath = localPath;
        File downFile = new File(outputFilePath);
        getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
    }
}
