package com.water.warning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 河道水位预警系统启动类
 */
@SpringBootApplication
@MapperScan("com.water.warning.modules.*.mapper")
@EnableScheduling
public class RiverWarningApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiverWarningApplication.class, args);
        System.out.println("============================================");
        System.out.println("  河道水位预警系统启动成功!");
        System.out.println("  API文档: http://localhost:8080/api/doc.html");
        System.out.println("============================================");
    }
}
