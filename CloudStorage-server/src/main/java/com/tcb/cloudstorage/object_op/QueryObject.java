package com.tcb.cloudstorage.object_op;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;

import java.util.List;

public class QueryObject
{
    private COSClient cosClient;

    public QueryObject(COSClient cosClient)
    {
        this.cosClient = cosClient;
    }

    public void listObjects(String BucketName, String AppId)
    {
        // Bucket 的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置 bucket 名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix 表示列出的 object 的 key 以 prefix 开始
        listObjectsRequest.setPrefix("Desktop/");
        // deliter 表示分隔符, 设置为/表示列出当前目录下的 object, 设置为空表示列出所有的 object
        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
                return;
            } catch (CosClientException e) {
                e.printStackTrace();
                return;
            }
            // common prefix 表示表示被 delimiter 截断的路径, 如 delimter 设置为/, common prefix 则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary 表示所有列出的 object 列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径 key
                String key = cosObjectSummary.getKey();
                // 文件的 etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();
                System.out.println("key："+key+" etag："+etag +" filesize："+fileSize+"  storageClasses："+storageClasses);
            }
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
    }
}
