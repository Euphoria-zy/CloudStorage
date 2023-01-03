package com.tcb.cloudstorage.object_op;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.*;
import com.tcb.cloudstorage.transfer_manager.TransferManagerManage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/*
*
* */
public class FileUpLoadWithTM
{
    //上传本地源文件
    /*
     * @param BucketName: 存储桶名
     * @param AppId: APPID
     * @param key: 对象键(Key),是对象在存储桶中的唯一标识。
     * @param path: 本地文件路径
     * @param transferManager: TransferManager实例
     * return void
     **/
    public void upLoadFile(String BucketName, String AppId, String key, String path, TransferManager transferManager)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;

        File localFile = new File(path);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        /*//若需要设置对象的自定义 Headers 可参照下列代码,若不需要可省略下面这几行,对象自定义 Headers 的详细信息可参考https://cloud.tencent.com/document/product/436/13361
        ObjectMetadata objectMetadata = new ObjectMetadata();

        //若设置Content-Type、Cache-Control、Content-Disposition、Content-Encoding、Expires这五个字自定义 Headers，推荐采用objectMetadata.setHeader()
        objectMetadata.setHeader(key, value);
        //若要设置 “x-cos-meta-[自定义后缀]” 这样的自定义 Header，推荐采用
        Map<String, String> userMeta = new HashMap<String, String>();
        userMeta.put("x-cos-meta-[自定义后缀]", "value");
        objectMetadata.setUserMetadata(userMeta);

        putObjectRequest.withMetadata(objectMetadata);*/

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            showUpLoadProgress(upload);
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 确定本进程不再使用 transferManager 实例之后，关闭之
        TransferManagerManage.shutdownTransferManager(transferManager);
    }

    //上传文件
    /*
     * @param BucketName: 存储桶名
     * @param AppId: APPID
     * @param key: 对象键(Key),是对象在存储桶中的唯一标识。
     * @param path: 本地文件路径
     * @param transferManager: TransferManager实例
     * return void
     **/
    public void upLoadFile(String BucketName, String AppId, String key, File file, TransferManager transferManager)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        /*//若需要设置对象的自定义 Headers 可参照下列代码,若不需要可省略下面这几行,对象自定义 Headers 的详细信息可参考https://cloud.tencent.com/document/product/436/13361
        ObjectMetadata objectMetadata = new ObjectMetadata();

        //若设置Content-Type、Cache-Control、Content-Disposition、Content-Encoding、Expires这五个字自定义 Headers，推荐采用objectMetadata.setHeader()
        objectMetadata.setHeader(key, value);
        //若要设置 “x-cos-meta-[自定义后缀]” 这样的自定义 Header，推荐采用
        Map<String, String> userMeta = new HashMap<String, String>();
        userMeta.put("x-cos-meta-[自定义后缀]", "value");
        objectMetadata.setUserMetadata(userMeta);

        putObjectRequest.withMetadata(objectMetadata);*/

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            showUpLoadProgress(upload);
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 确定本进程不再使用 transferManager 实例之后，关闭之
        TransferManagerManage.shutdownTransferManager(transferManager);
    }

    //上传流文件
    public void upLoadFile(String BucketName, String AppId, String key, InputStream inputStream, TransferManager transferManager)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId ;

        // 这里创建一个 ByteArrayInputStream 来作为示例，实际中这里应该是您要上传的 InputStream 类型的流
        long inputStreamLength = 0;
        try {
            //获取流的长度
            inputStreamLength = inputStream.available();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(inputStreamLength);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            //打印上传进度
            showUpLoadProgress(upload);
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TransferManagerManage.shutdownTransferManager(transferManager);
    }

    //上传目录
    /*
     * @param BucketName: 存储桶名
     * @param AppId: APPID
     * @param key: 对象键(Key),是对象在存储桶中的唯一标识。
     * @param cos_path: 上传到bucket之后的前缀目录，设置为 “”，表示上传到 bucket 的根目录
     * @param dir_path: 要上传的文件夹的绝对路径
     * @param transferManager: TransferManager实例
     * return void
     **/
    public void upLoadDir(String BucketName, String AppId, String cos_path, String dir_path, TransferManager transferManager)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BucketName +"-"+ AppId;

        try {
            //includeSubDirectory:是否递归上传目录下的子目录，如果是 true，子目录下的文件也会上传，且cos上会保持目录结构
            // 返回一个异步结果Upload, 可同步的调用waitForUploadResult等待upload结束, 成功返回UploadResult, 失败抛出异常.
            MultipleFileUpload upload = transferManager.uploadDirectory(bucketName, cos_path, new File(dir_path), true);

            // 查看上传进度，
            showUpLoadProgress(upload);

            // 或者阻塞等待完成
            upload.waitForCompletion();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 示例代码：关闭 TransferManager
        TransferManagerManage.shutdownTransferManager(transferManager);
    }

    //显示上传进度
    public void showUpLoadProgress(Transfer transfer)
    {
        // 这里的 Transfer 是异步上传结果 Upload 的父类
        System.out.println(transfer.getDescription());
        // transfer.isDone() 查询上传是否已经完成
        while (transfer.isDone() == false) {
            try {
                // 每 2 秒获取一次进度
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

            TransferProgress progress = transfer.getProgress();
            //获取已上传的字节数
            long sofar = progress.getBytesTransferred();
            //获取总文件的字节数
            long total = progress.getTotalBytesToTransfer();
            //获取已上传的字节百分比
            double pct = progress.getPercentTransferred();

            System.out.printf("upload progress: [%d / %d] = %.02f%%\n", sofar, total, pct);
        }

        // 完成了 Completed，或者失败了 Failed
        System.out.println("上传完成，状态：" + transfer.getState());
    }

}
