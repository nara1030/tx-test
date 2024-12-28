package org.among.trsnctest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.among.trsnctest.repository")
public class TrsncTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrsncTestApplication.class, args);
    }

}
