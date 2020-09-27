package com.tangokk.redisretry.springbootexample;

import com.tangokk.redisretry.lib.RetryService;
import com.tangokk.redisretry.lib.impl.SimpleRetryApi;
import com.tangokk.redisretry.springbootexample.dto.RetryItem;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetryDataProducer {

    @Autowired
    SimpleRetryApi retryApi;

    @Autowired
    RetryService retryService;

    boolean isRunning;

    @Scheduled(fixedDelay = 1000)
    public void startProduce() {
        retryApi.addDataToRetry(new RetryItem("" + System.currentTimeMillis()));
    }



    @PreDestroy
    public void onDestroy() {
        isRunning = false;
    }

}
