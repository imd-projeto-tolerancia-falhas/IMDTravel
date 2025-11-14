package com.imdtravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ImdTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImdTravelApplication.class, args);
    }

}
