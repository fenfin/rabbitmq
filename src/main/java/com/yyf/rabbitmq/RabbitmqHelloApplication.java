package com.yyf.rabbitmq;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableLogRecord(tenant = "cn.edu.hqu.atm")
public class RabbitmqHelloApplication {


    public static void main(String[] args) {
        SpringApplication.run(RabbitmqHelloApplication.class, args);
    }


}
