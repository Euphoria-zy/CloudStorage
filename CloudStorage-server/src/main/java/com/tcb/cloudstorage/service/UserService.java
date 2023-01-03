package com.tcb.cloudstorage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Service;


public interface UserService
{

    public boolean login(User user);

    public boolean register(User user);

    public boolean changePassword(String username, String oldPwd, String newPwd);
}
