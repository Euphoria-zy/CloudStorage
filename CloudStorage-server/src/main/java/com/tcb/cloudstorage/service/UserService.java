package com.tcb.cloudstorage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Service;


public interface UserService
{

    public boolean register(User user);

    public boolean changePassword(String username, String newPwd);

    public boolean compareCheckCode(User user, String checkCode);

    public void saveCheckCode(User user, String checkCode);

    public User getUserByUsernameAndPwd(User user);
    public User getUserByUsername(User user);
    public boolean updateUserInfo(User user);
    public User getUserById(int userId);
}
