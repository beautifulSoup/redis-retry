package com.tangokk.redisretry.lib;

import java.time.LocalDateTime;

public interface RetryApi<T> {

    /**
     * retry due items
     */
    void retry();

    /**
     * retry all items
     */
    void retryAll();


    void addDataToRetry(T t);

    /**
     *
     * @param t
     * @param retryTime the item will retry at the time
     */
    void addDataToRetry(T t, LocalDateTime retryTime);

    /**
     *
     * @param retryDelaySeconds set the default delay that the items will retry
     */
    void setRetryDelaySeconds(Long retryDelaySeconds);

}
