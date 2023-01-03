package com.tcb.cloudstorage.object_op;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class FileUpLoad
{
    private static String localFilePath = "C:\\Users\\29787\\Desktop\\vue.js.txt";
    private COSClient cosClient;

    public FileUpLoad(COSClient cosClient)
    {
        this.cosClient = cosClient;
    }
    public void upLoad(String BucketName, String AppId, String key)
    {
        // 指定要上传的文件
        File localFile = new File(localFilePath);
        // 指定文件将要存放的存储桶
        String bucketName = BucketName +"-"+ AppId;
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
    }

    /*
    * 对象存储中文件和目录都是对象，目录只是以/结尾的对象。创建文件时，不需要创建目录。
    * 如创建一个对象键为 xxx/yyy/zzz.txt的文件，只用把 key 设置为xxx/yyy/zzz.txt即可，不用建立xxx/yyy/这个对象。
    * 在控制台上展示时，也会以/作为分隔，展示出目录的层级效果。但这些目录对象是不存在的。
    * */
    public void createDir(String BucketName, String AppId, String key)
    {
        String bucketName = BucketName +"-"+ AppId;
        //创建一个folder/images/的目录
        // 目录对象即是一个/结尾的空文件，上传一个长度为 0 的 byte 流
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, key, input, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
    }
}
