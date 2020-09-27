package com.tangokk.redisretry.lib;

import java.time.LocalDateTime;
import java.util.List;

public interface RetryDataDao<T> {

    void saveRetryData(Object data, LocalDateTime retryTime);

    List<T> getAndRemoveDueItems();

    List<T> getAndRemoveAll();

}
