package com.tcb.cloudstorage.client;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;

/*
* 创建COS_Client对象,
* */
public class COS_Client
{
    private static COSClient cosClient = null;
    private static String SECRETID = "AKIDEKTYfbIkz1pCLI2WlFXdVAdsdAUbU8Yj";
    private static String SECRETKEY = "4tTpxBByYv43qRpzvqY3MRI196oSt8Ay";
    private static String COS_REGION = "ap-shanghai";
    public static COSClient createCOSClient()
    {
        if (cosClient == null)
        {
            // 1 初始化用户身份信息（secretId, secretKey）。// SECRETID和SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
            String secretId = SECRETID;
            String secretKey = SECRETKEY;
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
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
        }
        return cosClient;
    }
    //释放资源
    public static void releaseCosClient()
    {
        cosClient.shutdown();
    }
}
