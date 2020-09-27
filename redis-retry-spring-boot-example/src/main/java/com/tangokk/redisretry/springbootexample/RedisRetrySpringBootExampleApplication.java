package com.tangokk.redisretry.springbootexample;

import com.tangokk.redisretry.springbootstarter.EnableRedisRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRedisRetry
@EnableScheduling
public class RedisRetrySpringBootExampleApplication {


    public static void main(String[] args) {
        SpringApplication.run(RedisRetrySpringBootExampleApplication.class, args);
    }

}
