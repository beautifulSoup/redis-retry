package com.tangokk.redisretry.lib;


public interface RetryService<T> {

    void retry(T retryData);

}
