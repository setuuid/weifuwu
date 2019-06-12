package com.bit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Desc
 *
 * @author jianming.fan
 * @date 2018-11-28
 */
@SpringBootApplication(scanBasePackages = "com.bit")
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.bit.module.*.dao")
public class ServicePbApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicePbApplication.class, args);
    }
}