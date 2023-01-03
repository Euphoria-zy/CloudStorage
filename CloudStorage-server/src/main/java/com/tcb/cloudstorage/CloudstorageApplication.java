package com.tcb.cloudstorage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tcb.cloudstorage.mapper")
public class CloudstorageApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CloudstorageApplication.class, args);
    }

}
