package com.tcb.cloudstorage.filter;

import com.tcb.cloudstorage.domain.LoginUser;
import com.tcb.cloudstorage.utils.JWTUtil;
import com.tcb.cloudstorage.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
 * code for class JwtAuthenticationTokenFilter
 * @param null
 * 定义jwt认证过滤器，完成校验功能
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2022/10/12 21:47
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        //获取token
        String token = httpServletRequest.getHeader("access-token");
        if (!StringUtils.hasText(token)){
            //如果没有token，直接放行，后续的过滤器获取不到认证信息，也会失败
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //解析token
        String userId;
        try
        {
            Claims claims = JWTUtil.parseJwt(token);
            userId = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息

        String redisKey = "login:"+userId;
        LoginUser loginUser = (LoginUser) redisUtil.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder中，后面的过滤器链会从中获取认证信息

        //TODO 获取权限信息，封装到Authentication中。权限信息在登录时，在UserDetailsService中进行封装
        //此时LoginUser中已经有了权限信息,通过loginUser.getAuthorities()
        //能获取到token并获取到用户信息，说明当前用户已认证，则调用三个参数的构造方法，该构造方法会设置认证状态为true
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
