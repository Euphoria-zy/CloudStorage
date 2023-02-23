package com.tcb.cloudstorage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.FileStore;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import com.tcb.cloudstorage.service.FileStoreService;
import com.tcb.cloudstorage.service.UserService;
import com.tcb.cloudstorage.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileStoreService fileStoreService;

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
            User user = User.builder().userId(root.getUserId()).username(username).password(newPwd).build();
            userMapper.update(user, lambdaQueryWrapper);
            return true;
        }
        return false;
    }

    @Override
    public boolean compareCheckCode(User user, String checkCode)
    {
        String key = "CheckCode"+user.getUsername();
        Jedis jedisPool = JedisUtils.getJedisPool();
        String code = jedisPool.get(key);
        if (code == "" || code == null) 
            return false;
        else if (code.equals(checkCode)) {
            jedisPool.del(key);
            return true;
        }
        else 
            return false;
    }

    @Override
    public void saveCheckCode(User user, String checkCode)
    {
        String key = "CheckCode"+user.getUsername();
        Jedis jedisPool = JedisUtils.getJedisPool();
        //设置过期时间，2分钟
        jedisPool.setex(key, 2*60, checkCode);
        jedisPool.close();
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

}
