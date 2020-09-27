package com.tangokk.redisretry.lib.impl;


import com.tangokk.redisretry.lib.RetryApi;
import com.tangokk.redisretry.lib.RetryDataDao;
import com.tangokk.redisretry.lib.RetryService;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleRetryApi implements RetryApi<Object> {

    Logger logger = LoggerFactory.getLogger(SimpleRetryApi.class);


    private static final long DEFAULT_DELAY_RETRY_SECONDS = 600;

    Long retryDelaySeconds = DEFAULT_DELAY_RETRY_SECONDS;

    RetryService<Object> retryService;

    RetryDataDao<Object> retryDataDao;

    public SimpleRetryApi(RetryDataDao<Object> retryDataDao) {
        this.retryDataDao = retryDataDao;
    }


    public void setRetryService(RetryService<Object> retryService) {
        this.retryService = retryService;
    }

    @Override
    public void setRetryDelaySeconds(Long retryDelaySeconds) {
        this.retryDelaySeconds = retryDelaySeconds;
    }


    @Override
    public void retry() {
        List<Object> dataToRetry = retryDataDao.getAndRemoveDueItems();
        logger.info("开始重试，总数：{}", dataToRetry.size());
        for(Object d : dataToRetry) {
            retry(d);
        }
    }

    @Override
    public void retryAll() {
        List<Object> dataToRetry = retryDataDao.getAndRemoveAll();
        logger.info("开始重试，总数：{}", dataToRetry.size());
        for(Object d : dataToRetry) {
            retry(d);
        }
    }

    @Override
    public void addDataToRetry(Object t) {
        addDataToRetry(t, LocalDateTime.now().plusSeconds(retryDelaySeconds));
    }

    @Override
    public void addDataToRetry(Object t, LocalDateTime retryTime) {
        retryDataDao.saveRetryData(t, retryTime);
    }

    private void retry(Object t) {
        if(retryService == null) {
            throw new RuntimeException("重试失败，没有设置RetryService");
        }
        try {
            retryService.retry(t);
        } catch (Exception e) {
            logger.error("重试失败，重新加入重试队列", e);
            retryDataDao.saveRetryData(t, LocalDateTime.now().plusSeconds(retryDelaySeconds));
        }
    }

}
