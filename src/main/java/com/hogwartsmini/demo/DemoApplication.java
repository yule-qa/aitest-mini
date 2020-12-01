package com.hogwartsmini.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.hogwartsmini.demo.dao")  //要扫描哪个包
public class DemoApplication {

    public static void main(String[] args) {
         SpringApplication.run(DemoApplication.class, args);
    }

}
