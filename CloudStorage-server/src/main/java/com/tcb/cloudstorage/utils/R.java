package com.tcb.cloudstorage.utils;

import lombok.Data;

/*
 * code for class R
 * @param null
 * 数据一致性处理
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2022/12/23 20:08
 **/
@Data
public class R
{
    private boolean flag;
    private String msg;
    private Object data;
    public R(){}
    public R(boolean flag)
    {
        this.flag = flag;
    }

    public R(boolean flag, String msg)
    {
        this.flag = flag;
        this.msg = msg;
    }

    public R(boolean flag, String msg, Object data)
    {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }
}
