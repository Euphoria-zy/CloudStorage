package com.tcb.cloudstorage.service;

import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.utils.R;

public interface LoginService
{
    R login(User user);

    R loginOut();
}
