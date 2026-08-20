package com.renewalguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RenewalGuardApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                RenewalGuardApplication.class,
                args
        );
    }
}
