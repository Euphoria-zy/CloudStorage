package com.tcb.cloudstorage.handler;

import com.tcb.cloudstorage.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginHandler implements HandlerInterceptor
{

    /**
     * @Description 登录拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        HttpSession session = request.getSession(true);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null){
            response.sendRedirect("/login.html");
            return false;
        }else
            return true;
    }
}
