package com.tcb.cloudstorage.object_op;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.tcb.cloudstorage.transfer_manager.TransferManagerManage;
import java.io.File;

public class FileDownLoadWithTM
{
    /*
     * @param BucketName: 存储桶名
     * @param AppId: APPID
     * @param key: 对象键(Key),是对象在存储桶中的唯一标识。
     * @param localPath: 本地文件路径
     * @param transferManager: TransferManager实例
     * return void
     **/
    public void downLoad(String BucketName, String AppId, String key, String localPath, TransferManager transferManager)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;
        // 本地文件路径
        File downloadFile = new File(localPath);

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        try {
            // 返回一个异步结果 Download, 可同步的调用 waitForCompletion 等待下载结束, 成功返回 void, 失败抛出异常
            Download download = transferManager.download(getObjectRequest, downloadFile);
            download.waitForCompletion();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TransferManagerManage.shutdownTransferManager(transferManager);
    }
}
