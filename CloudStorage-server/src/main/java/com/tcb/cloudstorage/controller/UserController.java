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
import com.tcb.cloudstorage.utils.CheckCodeGenerate;
import com.tcb.cloudstorage.utils.EmailService;
import com.tcb.cloudstorage.utils.MultipartFileToFile;
import com.tcb.cloudstorage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController
{
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public R login(User user)
    {
        User userInfo = userService.getUserInfo(user);
        if (userInfo != null){
            session.setAttribute("loginUser", userInfo);
            System.out.println("登陆成功:"+userInfo.getUsername());
            return new R(true, "登陆成功，跳转到首页!");
        } else
            return new R(false, "登录失败,用户名或密码错误!");
    }

    @RequestMapping("/register")
    public R register(String username, String password, String email, String checkCode)
    {
        User user = new User(username,password,email);
        boolean b = userService.compareCheckCode(user, checkCode);
        if (b){
            boolean register = userService.register(user);
            if (register)
                return new R(true, "注册成功，返回登录页面!");
            else
                return new R(false, "注册失败，用户名已存在!");
        }else {
            return new R(false, "验证码错误，请重新输入!");
        }
    }

    @RequestMapping("/forgetPwd")
    public R forgetPwd(String username, String newPwd, String checkCode)
    {
        User user = new User();
        user.setUsername(username);
        boolean b1 = userService.compareCheckCode(user, checkCode);
        if (b1)
        {
            boolean b2 = userService.changePassword(username, newPwd);
            if (b2) {
                return new R(true, "成功找回密码，请重新登录!");
            }
            else {
                return new R(false, "该用户不存在!");
            }
        }else
            return new R(false, "验证码错误，请重新输入!");
    }

    //注册验证码
    @RequestMapping("/getRegisterCode")
    public R sendRegisterCode(String username, String email)
    {
        System.out.println("电子邮箱"+email);
        System.out.println("用户名"+username);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        try
        {
            //生成验证码
            String checkCode = CheckCodeGenerate.getCheckCode();
            //保存到redis中
            userService.saveCheckCode(user, checkCode);
            //同时发送到用户邮箱
            EmailService.sendEmail(user, checkCode, 1);
        } catch (GeneralSecurityException e)
        {
            e.printStackTrace();
            return new R(false, "请输入正确的电子邮箱!");
        }
        return new R(true, "验证码发送成功!");
    }

    //找回密码验证码
    @RequestMapping("/getChangePwdCode")
    public R sendChangePwdCode(String username)
    {
        User user = new User();
        user.setUsername(username);
        //根据用户名查询用户完整信息
        User userInfo = userService.getUserInfo(user);
        if (userInfo == null)
            return new R(false, "该用户不存在");
        try
        {
            //生成验证码
            String checkCode = CheckCodeGenerate.getCheckCode();
            //保存到redis中
            userService.saveCheckCode(userInfo, checkCode);
            //同时发送到用户邮箱
            EmailService.sendEmail(userInfo, checkCode, 2);
        } catch (GeneralSecurityException e)
        {
            e.printStackTrace();
            return new R(false, "验证码发送失败!");
        }
        return new R(true, "验证码发送成功!");
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
