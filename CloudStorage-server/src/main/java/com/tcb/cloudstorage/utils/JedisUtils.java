package com.tcb.cloudstorage.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisUtils
{
    private static JedisPool jedisPool;
    static {
        //1、加载配置文件
        InputStream resourceAsStream = JedisUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        Properties properties=new Properties();
        try
        {
            properties.load(resourceAsStream);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        jedisPoolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxIdle")));
        String host=properties.getProperty("host");
        Integer port=Integer.parseInt(properties.getProperty("port"));
        jedisPool=new JedisPool(jedisPoolConfig,host,port);
    }
    public static Jedis getJedisPool()
    {
        return jedisPool.getResource();
    }
}
