package com.tcb.cloudstorage.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginUser implements UserDetails
{
    private User user;

    //待封装的权限信息
    private List<String> permissions;

    //存入redis对权限信息不进行序列化
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(User user, List<String> permission)
    {
        this.user = user;
        this.permissions = permission;
    }

    //返回权限信息，将自定义的权限信息以内置的权限类封装后返回
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        if (permissions == null)
            return null;
        if (authorities != null)
            return authorities;
        /*List<GrantedAuthority> permits = new ArrayList<>();
        for (String permission: permissions)
        {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission);
            permits.add(grantedAuthority);
        }*/
        //函数式编程写法
        authorities = permissions
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
