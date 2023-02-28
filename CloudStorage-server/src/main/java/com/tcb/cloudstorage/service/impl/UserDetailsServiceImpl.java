package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tcb.cloudstorage.domain.LoginUser;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/*
 * code for class UserDetailsServiceImpl
 * @param null
 * 自定义UserDetailsService实现类，实现从数据库查询用户名密码完成校验功能
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2022/10/12 13:57
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

    @Autowired
    private UserMapper userMapper;

    /*
    * 查询相关的用户信息以及权限信息，封装为UserDetails对象
    * */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        //查询用户信息，仅实现了对用户名的校验
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, s);
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        return new LoginUser(user,null);

        // TODO :查询权限信息

        /*
        * 将获取到的用户信息以及权限信息封装成UserDetails对象返回后，AbstractUserDetailsAuthenticationProvider调用
        * additionalAuthenticationChecks方法对用户的密码进行最后的检查
         * */
    }
}
