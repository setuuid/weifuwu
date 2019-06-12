package com.bit.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description:  文件服务
 * @Author: liyujun
 * @Date: 2018-10-11
 **/
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.bit.file.dao")
@SpringBootApplication
public class FileServiceApplication {
    public static void main( String[] args )
    {
        SpringApplication.run(FileServiceApplication.class, args);
    }
}
