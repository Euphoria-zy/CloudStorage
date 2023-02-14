package com.tcb.cloudstorage.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.Headers;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.*;
import com.qcloud.cos.utils.DateUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class COSUtils
{
    //存储桶名称
    public static String BUCKET_NAME = "examplebucket";
    public static String APP_ID = "1311199809";

    private static String SECRETID;
    private static String SECRETKEY;
    private static String SESSION_TOKEN;
    private static String COS_REGION = "ap-shanghai";
    private static COSClient cosClient;
    private static TransferManager transferManager;
    public static void initConnection()
    {
        //创建COSClient对象
        RestTemplate restTemplate = new RestTemplate();
        Map forObject = restTemplate.getForObject("http://localhost:8085/getSecretKey", Map.class);
        SECRETID = (String) forObject.get("SecretId");
        SECRETKEY = (String) forObject.get("SecretKey");
        SESSION_TOKEN = (String) forObject.get("sessionToken");
        cosClient = createCOSClient();
        transferManager = createTransferManager(cosClient);
    }


    /*
     * @Description: 上传文件
     * @param file: 上传的文件
     * @param key: 对象键(Key),是对象在存储桶中的唯一标识(路径)。
     * return void
     **/
    public static boolean uploadFile(String key, MultipartFile multipartFile)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BUCKET_NAME +"-"+ APP_ID;
        initConnection();
        File file = MultipartFileToFile.MultipartFileToFile(multipartFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);

        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            showUpLoadProgress(upload);
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (CosServiceException e) {
            e.printStackTrace();
            return false;
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static URL downLoadFileWithURL(String key, String fileName)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BUCKET_NAME +"-"+ APP_ID;
        initConnection();
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);

        // 设置下载时返回的 http 头
        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        //String responseContentType = "image/x-icon";
        String responseContentLanguage = "zh-CN";
        // 设置返回头部里包含文件名信息
        //设置成下载形式为附件以及默认文件名
        String responseContentDisposition = "attachment;filename="+fileName;
        String responseCacheControl = "no-cache";
        //设置请求过期时间为1小时
        String cacheExpireStr =
                DateUtils.formatRFC822Date(new Date(System.currentTimeMillis() + 24L * 3600L * 1000L));
        //responseHeaders.setContentType(responseContentType);
        responseHeaders.setContentLanguage(responseContentLanguage);
        responseHeaders.setContentDisposition(responseContentDisposition);
        responseHeaders.setCacheControl(responseCacheControl);
        responseHeaders.setExpires(cacheExpireStr);

        req.setResponseHeaders(responseHeaders);

        // 设置签名过期时间(可选)，若未进行设置，则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名过期时间为30分钟
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
        req.setExpiration(expirationDate);
        // 填写本次请求的头部
        // host 必填
        req.putCustomRequestHeader(Headers.HOST, cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(bucketName));

        URL url = cosClient.generatePresignedUrl(req);
        System.out.println(url.toString());

        return url;
    }

    //删除单个文件
    public static boolean deleteFile(String key)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BUCKET_NAME +"-"+ APP_ID;
        initConnection();
        try {
            cosClient.deleteObject(bucketName, key);
        } catch (CosServiceException e) {
            e.printStackTrace();
            return false;
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyFile(String sourceKey, String destinationKey)
    {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = BUCKET_NAME +"-"+ APP_ID;
        initConnection();
        // 2 bucket 的地域
        Region srcBucketRegion  = new Region(COS_REGION);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(srcBucketRegion, bucketName,
                sourceKey, bucketName, destinationKey);
        try {
            Copy copy = transferManager.copy(copyObjectRequest);
            // 高级接口会返回一个异步结果 Copy
            // 可同步的调用 waitForCopyResult 等待复制结束, 成功返回 CopyResult, 失败抛出异常
            CopyResult copyResult = copy.waitForCopyResult();
        } catch (CosServiceException e) {
            e.printStackTrace();
            return false;
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static COSClient createCOSClient()
    {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String tmpSecretId = SECRETID;
        String tmpSecretKey = SECRETKEY;
        String sessionToken = SESSION_TOKEN;

        COSCredentials cred = new BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken);
        // 2 设置 bucket 的地域
        Region region = new Region(COS_REGION);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 以下的设置，是可选的：
        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30*1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30*1000);

        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    //显示上传进度
    public static void showUpLoadProgress(Transfer transfer)
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

    //创建TransferManager实例
    public static TransferManager createTransferManager(COSClient cosClient)
    {
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(10*1024*1024);
        transferManagerConfiguration.setMinimumUploadPartSize(5*1024*1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }

    //关闭资源
    public static void shutdownTransferManager() {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }


}
