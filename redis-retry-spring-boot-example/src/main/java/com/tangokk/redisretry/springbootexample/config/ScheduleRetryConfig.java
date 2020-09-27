package com.tangokk.redisretry.springbootexample.config;


import com.tangokk.redisretry.lib.impl.SimpleRetryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleRetryConfig {

    @Autowired
    SimpleRetryApi api;

    @Scheduled(fixedDelay = 10000)
    public void retry() {
        api.retry();
    }


}
