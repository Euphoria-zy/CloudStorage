package com.tcb.cloudstorage.controller;

import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.service.LoginService;
import com.tcb.cloudstorage.service.UserService;
import com.tcb.cloudstorage.utils.COSUtils;
import com.tcb.cloudstorage.utils.CheckCodeGenerate;
import com.tcb.cloudstorage.utils.EmailService;
import com.tcb.cloudstorage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController
{
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    /**
     * @Description 登录功能
     * @param user
     * @return
     */
    @RequestMapping("/login")
    public R login(@RequestBody User user)
    {
        R login = loginService.login(user);
        return login;
    }

    /**
     * @Description 注册功能
     * @param username
     * @param password
     * @param email
     * @param checkCode
     * @return
     */
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

    /**
     * @Description 找回密码功能
     * @param username
     * @param newPwd
     * @param checkCode
     * @return
     */
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

    @RequestMapping("/uploadHeadImage")
    public R uploadHeadImage(@RequestParam MultipartFile headImage)
    {
        String key = loginUser.getUsername()+"/"+"headImage"+headImage.getOriginalFilename();
        boolean b1 = COSUtils.uploadFile(key, headImage);
        if (b1) {
            URL viewObjectUrl = COSUtils.getViewObjectUrl(key);
            User user = loginUser;
            user.setImagePath(viewObjectUrl.toString());
            boolean b2 = userService.updateUserInfo(user);
            if (b2) {
                Map<String, Object> map = new HashMap<>();
                map.put("headImage", viewObjectUrl.toString());
                map.put("currentUser", user);
                return new R(true, "上传头像成功!", map);
            } else {
                COSUtils.deleteFile(key);
                return new R(false, "上传头像失败，请重新上传!");
            }
        }else
            return new R(false,"上传头像是失败!");
    }

    /**
     * @Description 获取注册验证码
     * @param username
     * @param email
     * @return
     */
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

    /**
     * @Description 获取找回密码验证码
     * @param username
     * @return
     */
    @RequestMapping("/getChangePwdCode")
    public R sendChangePwdCode(String username)
    {
        User user = new User();
        user.setUsername(username);
        //根据用户名查询用户完整信息
        User userInfo = userService.getUserByUsername(user);
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

    @RequestMapping("/loginOut")
    public R loginOut()
    {
        R loginOut = loginService.loginOut();
        return loginOut;
    }
}
