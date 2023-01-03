package com.tcb.cloudstorage.bucket_op;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;

import java.util.List;

/*
* 创建存储桶、查询存储桶
* */
public class BucketOperation
{
    private COSClient cosClient;
    private Bucket bucketResult;
    public static String APPID = "1311199809";
    public static String BUCKET_NAME = "examplebucket";

    public BucketOperation(COSClient cosClient)
    {
        this.cosClient = cosClient;
    }

    public void createBucket()
    {
        String bucket = BUCKET_NAME +"-"+ APPID; //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
        createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
        try{
            bucketResult = cosClient.createBucket(createBucketRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    public void showBucket()
    {
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucketElement : buckets)
        {
            String bucketName = bucketElement.getName();
            String bucketLocation = bucketElement.getLocation();
            System.out.println(bucketName + "-----" + bucketLocation);
        }
    }
}
