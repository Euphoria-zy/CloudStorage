package com.tcb.cloudstorage.controller;

import com.tcb.cloudstorage.domain.User;
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
        this.session = request.getSession(true);
        this.loginUser = (User) session.getAttribute("loginUser");
    }
}
