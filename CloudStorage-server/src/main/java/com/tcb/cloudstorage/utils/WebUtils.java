package com.tcb.cloudstorage.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils
{

    //将字符串渲染到前端
    public static String renderString(HttpServletResponse response, String str)
    {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try
        {
            response.getWriter().print(str);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
