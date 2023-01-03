package com.tcb.cloudstorage.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.transfer.TransferManager;
import com.tcb.cloudstorage.bucket_op.BucketOperation;
import com.tcb.cloudstorage.client.COS_Client;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import com.tcb.cloudstorage.object_op.FileUpLoadWithTM;
import com.tcb.cloudstorage.service.UserService;
import com.tcb.cloudstorage.transfer_manager.TransferManagerManage;
import com.tcb.cloudstorage.utils.MultipartFileToFile;
import com.tcb.cloudstorage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public R login(User user)
    {
        boolean login = userService.login(user);
        if (login)
            return new R(true, "登陆成功!");
        else
            return new R(false, "登录失败!");
    }

    @RequestMapping("/register")
    public R register(User user)
    {
        boolean register = userService.register(user);
        if (register)
            return new R(true, "注册成功，返回登录页面!");
        else
            return new R(false, "注册失败，用户名已存在!");
    }

    @RequestMapping("/changePwd")
    public R changePwd(String username, String oldPwd, String newPwd)
    {
        boolean b = userService.changePassword(username, oldPwd, newPwd);
        if (b)
            return new R(true, "密码更新成功，请重新登录!");
        else
            return new R(false, "密码错误，请重试!");
    }

    @RequestMapping("/fileUpLoad")
    public R upLoadFile(@RequestParam MultipartFile multipart) throws IOException
    {
        String BucketName = BucketOperation.BUCKET_NAME;
        String AppId = BucketOperation.APPID;
        String key = multipart.getOriginalFilename();
        System.out.println("文件名："+key);
        File file = MultipartFileToFile.MultipartFileToFile(multipart);
        COSClient cosClient = COS_Client.createCOSClient();
        FileUpLoadWithTM fileUpLoadWithTM = new FileUpLoadWithTM();
        fileUpLoadWithTM.upLoadFile(BucketName, AppId, key, file, TransferManagerManage.createTransferManager(cosClient));
        return new R(true,"上传成功!");
    }
}
