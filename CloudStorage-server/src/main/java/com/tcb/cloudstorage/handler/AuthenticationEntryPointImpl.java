package com.tcb.cloudstorage.handler;

import com.alibaba.fastjson.JSON;
import com.tcb.cloudstorage.utils.R;
import com.tcb.cloudstorage.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * code for class AuthenticationEntryPointImpl
 * @param null
 * 自定义认证异常处理类
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2022/10/14 15:30
 **/
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint
{
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException
    {
        //处理认证异常
        R r = new R(false,"用户认证失败，请重新登录","402");
        String json = JSON.toJSONString(r);
        WebUtils.renderString(httpServletResponse,json);
    }
}
