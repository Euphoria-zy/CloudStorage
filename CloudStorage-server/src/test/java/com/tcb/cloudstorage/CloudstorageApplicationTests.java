package com.tcb.cloudstorage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tcb.cloudstorage.domain.FileStore;
import com.tcb.cloudstorage.domain.Folder;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.mapper.FolderMapper;
import com.tcb.cloudstorage.mapper.UserMapper;
import com.tcb.cloudstorage.service.FileStoreService;
import com.tcb.cloudstorage.utils.EmailService;
import com.tcb.cloudstorage.utils.JedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.security.GeneralSecurityException;

@SpringBootTest
class CloudstorageApplicationTests
{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private FolderMapper folderMapper;
    @Test
    void sendEmail() throws GeneralSecurityException
    {
        User user = new User();
        user.setUsername("Mike");
        user.setEmail("zh99499@163.com");
        user.setPassword("123456789");
        userMapper.insertUser(user);
        System.out.println(user.getUserId());
    }
    @Test
    void testRedis()
    {
        Jedis jedisPool = JedisUtils.getJedisPool();
        jedisPool.set("root","1213141");
        jedisPool.close();
    }
    @Test
    void testAddFileStore()
    {
        User user = new User();
        user.setUsername("Mike");
        user.setEmail("zh99499@163.com");
        user.setPassword("123456789");
        userMapper.insertUser(user);
        FileStore fileStore = FileStore.builder().userId(user.getUserId()).currentSize(0).build();
        fileStoreService.addFileStore(fileStore);
        user.setFileStoreId(fileStore.getFileStoreId());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        userMapper.update(user,lambdaQueryWrapper);
    }
    @Test
    void testSelectFolder()
    {
        int folderId = 1;
        Folder folder = folderMapper.selectById(folderId);
        System.out.println(folder);
    }
}
