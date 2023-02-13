package com.tcb.cloudstorage.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "user")
public class User
{
    //用户id
    @TableId(value = "user_id")
    private int userId;

    //用户名
    private String username;

    //用户密码
    private String password;

    //用户邮箱
    private String email;

    //用户文件仓库id
    private int fileStoreId;

    //用户头像地址
    private String imagePath;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
