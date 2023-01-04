package com.tcb.cloudstorage;

import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.utils.EmailService;
import com.tcb.cloudstorage.utils.JedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.security.GeneralSecurityException;

@SpringBootTest
class CloudstorageApplicationTests
{

    @Test
    void sendEmail() throws GeneralSecurityException
    {
        User user = new User();
        user.setUsername("Mike");
        user.setEmail("zh99499@163.com");
        user.setPassword("123456789");
        EmailService.sendEmail(user,"12131");
    }
    @Test
    void testRedis()
    {
        Jedis jedisPool = JedisUtils.getJedisPool();
        jedisPool.set("root","1213141");
        jedisPool.close();
    }

}
