package com.tcb.cloudstorage.handler;

import com.alibaba.fastjson.JSON;
import com.tcb.cloudstorage.utils.R;
import com.tcb.cloudstorage.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * code for class AccessDeniedHandlerImpl
 * @param null
 * 自定义授权异常处理类
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2022/10/14 15:29
 **/
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler
{
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException
    {
        //处理授权异常
        R r = new R(false,"您的权限不足", "403");
        String json = JSON.toJSONString(r);
        WebUtils.renderString(httpServletResponse, json);
    }
}
