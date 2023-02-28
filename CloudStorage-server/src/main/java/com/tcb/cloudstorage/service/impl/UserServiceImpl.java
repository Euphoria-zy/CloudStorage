package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.FileStore;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import com.tcb.cloudstorage.service.FileStoreService;
import com.tcb.cloudstorage.service.UserService;
import com.tcb.cloudstorage.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //注册
    @Override
    public boolean register(User user)
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User root = userMapper.selectOne(lambdaQueryWrapper);
        if (root != null)
            return false;
        else {
            String encode = passwordEncoder.encode(user.getPassword());
            user.setPassword(encode);
            if (userMapper.insertUser(user) > 0){
                //注册成功初始化文件仓库
                FileStore fileStore = FileStore.builder().userId(user.getUserId()).currentSize(0).build();
                fileStoreService.addFileStore(fileStore);
                user.setFileStoreId(fileStore.getFileStoreId());
                userMapper.update(user, lambdaQueryWrapper);
                System.out.println("注册成功--用户文件仓库初始化成功："+fileStore.getFileStoreId());
                return true;
            }else
                return false;
        }
    }

    //更改密码
    @Override
    public boolean changePassword(String username, String newPwd)
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User root = userMapper.selectOne(lambdaQueryWrapper);
        if (root != null) {
            String encode = passwordEncoder.encode(newPwd);
            root.setPassword(encode);
            userMapper.updateById(root);
            return true;
        }
        return false;
    }

    @Override
    public boolean compareCheckCode(User user, String checkCode)
    {
        String key = "CheckCode"+user.getUsername();
        String code = (String) redisUtil.getCacheObject(key);
        if (code == "" || code == null) 
            return false;
        else if (code.equals(checkCode)) {
            redisUtil.deleteObject(key);
            return true;
        }
        else 
            return false;
    }

    @Override
    public void saveCheckCode(User user, String checkCode)
    {
        String key = "CheckCode"+user.getUsername();
        //设置过期时间，2分钟
        redisUtil.setCacheObject(key,checkCode,2, TimeUnit.MINUTES);
    }

    @Override
    public User getUserByUsernameAndPwd(User user)
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        lambdaQueryWrapper.eq(User::getPassword, user.getPassword());
        User root = userMapper.selectOne(lambdaQueryWrapper);
        return root;
    }

    @Override
    public User getUserByUsername(User user)
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User root = userMapper.selectOne(lambdaQueryWrapper);
        return root;
    }

    @Override
    public boolean updateUserInfo(User user)
    {
        return userMapper.updateById(user)>0;
    }

    @Override
    public User getUserById(int userId)
    {
        return userMapper.selectById(userId);
    }

}
