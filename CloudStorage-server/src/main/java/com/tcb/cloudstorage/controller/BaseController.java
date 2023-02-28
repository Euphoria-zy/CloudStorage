package com.tcb.cloudstorage.controller;

import com.tcb.cloudstorage.domain.LoginUser;
import com.tcb.cloudstorage.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class BaseController
{

    protected HttpSession session;
    protected User loginUser;


    /**
     * 在每一个@RequestMapping标注的方法前执行
     * @param request
     * @param response
     */
    @ModelAttribute
    public void ReqAndResp(HttpServletRequest request, HttpServletResponse response)
    {
        //获取SecurityContextHolder中的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            throw new RuntimeException();
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            LoginUser loginInfo = (LoginUser) authentication.getPrincipal();
            this.loginUser = loginInfo.getUser();
        }
    }
}
