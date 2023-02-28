package com.tcb.cloudstorage.config;

import com.tcb.cloudstorage.filter.JwtAuthenticationTokenFilter;
import com.tcb.cloudstorage.handler.AccessDeniedHandlerImpl;
import com.tcb.cloudstorage.handler.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * code for class SpringSecurityConfig
 * @param null
 * 将BcryptPasswordEncoder注入到Spring容器中
 * @version 1.0.0
 * @return 
 * @author Zhou Yun
 * @date 2022/10/12 14:43
 **/
@Configuration
//开启权限注解的功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl deniedHandler;
    //设定加密方式
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    //获取AuthenticationManager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    //放行操作
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                //关闭csrf：前后端分离项目不需要开启CSRF
                .csrf().disable()
                //不通过session获取SecurityContext；即不通过session获取认证信息
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //对于登录接口，允许匿名访问
                .antMatchers("/user/login","/user/register","/user/forgetPwd","/user/getRegisterCode","/user/getChangePwdCode")
                //匿名访问，未登录状态可以访问；登录状态不可访问.permitAll()则是直接放行
                .anonymous()
                //除上面的请求，均需要权限认证
                .anyRequest().authenticated();

        //配置jwt过滤器，将在SpringSecurity过滤器链中UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http
                .exceptionHandling()
                //配置认证失败处理器
                .accessDeniedHandler(deniedHandler)
                //配置授权失败处理器
                .authenticationEntryPoint(authenticationEntryPoint);

        //开启SpringSecurity跨域
        http.cors();
    }
}
