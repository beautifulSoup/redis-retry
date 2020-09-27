package com.tangokk.redisretry.springbootexample.config;

import com.tangokk.redisretry.lib.RetryService;
import com.tangokk.redisretry.springbootexample.dto.RetryItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedisRetryConfig {



    @Bean
    public RetryService<Object> retryService() {
        RetryService retryService = retryData -> {
            if(retryData instanceof RetryItem) {
                log.debug("retry: {}", ((RetryItem) retryData).getKey());
            }

        };
        return retryService;
    }

}
