package com.tcb.cloudstorage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import com.tcb.cloudstorage.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{
    @Autowired
    private UserMapper userMapper;

    //登陆
    @Override
    public boolean login(User user)
    {
        User root = getUserInfo(user);
        if (root != null)
            return true;
        else
            return false;
    }

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
            userMapper.insert(user);
            return true;
        }
    }

    //更改密码
    @Override
    public boolean changePassword(String username, String oldPwd, String newPwd)
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername, username);
        lambdaQueryWrapper.eq(User::getPassword, oldPwd);
        User root = userMapper.selectOne(lambdaQueryWrapper);
        if (root != null) {
            User user = new User(username, newPwd);
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
        else if (code.equals(checkCode))
            return true;
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
    public User getUserInfo(User user)
    {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        lambdaQueryWrapper.eq(User::getPassword, user.getPassword());
        User root = userMapper.selectOne(lambdaQueryWrapper);
        return root;
    }

}
