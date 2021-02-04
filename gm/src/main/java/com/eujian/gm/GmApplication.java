package com.eujian.gm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.eujian.gm.mapper")
@SpringBootApplication
public class GmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmApplication.class, args);
    }

}
