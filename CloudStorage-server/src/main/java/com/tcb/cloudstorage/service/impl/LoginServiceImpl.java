package com.tcb.cloudstorage.service.impl;

import com.tcb.cloudstorage.domain.LoginUser;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.service.LoginService;
import com.tcb.cloudstorage.utils.JWTUtil;
import com.tcb.cloudstorage.utils.R;
import com.tcb.cloudstorage.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService
{
    @Autowired
    private RedisUtil redisUtil;
    //在SpringSecurityConfig中注入AuthenticationManager
    @Autowired
    private AuthenticationManager manager;

    @Override
    public R login(User user)
    {
        //传入Authentication的实现类，传入用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        //AuthenticationManager authenticate进行用户认证；
        Authentication authenticate = manager.authenticate(authenticationToken);
        // 如果认证不通过，抛出异常；
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过，则生成jwt，并用R对象封装返回；底层调用UserDetailsServiceImpl返回LoginUser对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = String.valueOf(loginUser.getUser().getUserId());
        String jwt = JWTUtil.createJwt(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        map.put("currentUser", loginUser.getUser());
        // 将用户信息存入redis
        redisUtil.setCacheObject("login:"+userId, loginUser);
        return new R(true,"登陆成功",map);
    }

    @Override
    public R loginOut()
    {
        //获取SecurityContextHolder中的用户信息
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        int userId = loginUser.getUser().getUserId();

        //删除redis中的信息
        redisUtil.deleteObject("login:"+userId);
        return new R(true, "注销登录成功");
    }
}
