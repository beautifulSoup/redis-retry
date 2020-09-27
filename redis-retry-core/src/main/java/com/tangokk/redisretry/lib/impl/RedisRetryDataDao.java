package com.tangokk.redisretry.lib.impl;

import com.tangokk.redisretry.lib.RetryDataDao;
import com.tangokk.redisretry.lib.serialize.RedisSerializer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class RedisRetryDataDao implements RetryDataDao<Object> {
    Logger logger = LoggerFactory.getLogger(RedisRetryDataDao.class);

    Integer redisDatabase;

    String redisKey;

    JedisPool pool;

    RedisSerializer redisSerializer;

    public RedisRetryDataDao(Integer redisDatabase, String key, JedisPool jedisPool, RedisSerializer<?> serializer) {
        this.redisDatabase = redisDatabase;
        this.redisKey = key;
        this.pool = jedisPool;
        this.redisSerializer = serializer;
    }

    @Override
    public void saveRetryData(Object data, LocalDateTime retryTime) {
        int maxRetryTime = 3;
        int tryTime = 0;
        while(tryTime < maxRetryTime) {
            tryTime ++;
            boolean ret = addData(data, retryTime);
            if(ret) {
                return;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }


    private boolean addData(Object data, LocalDateTime retryTime) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.select(redisDatabase);
            byte[] json = redisSerializer.serialize(data);
            jedis.zadd(redisKey.getBytes(), retryTime.toEpochSecond(ZoneOffset
                .of("+8")), json);
            return true;
        } catch (JedisConnectionException e) {
            logger.error("wtf", e);
            return false;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public List<Object> getAndRemoveDueItems() {
        try {
            LocalDateTime now = LocalDateTime.now();
            return getAndRemoveAccordingToScore(0d, now.toEpochSecond(ZoneOffset.of("+8")));
        } catch (JedisConnectionException e) {
            logger.error("wtf", e);
            return Collections.emptyList();
        }

    }

    @Override
    public List<Object> getAndRemoveAll() {
       return getAndRemoveAccordingToScore(0, -1);
    }


    private List<Object> getAndRemoveAccordingToScore(double min, double max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.select(redisDatabase);
            Set<byte[]> list = jedis.zrangeByScore(redisKey.getBytes(), min, max);
            List<Object> retList = list.stream()
                .map(o -> redisSerializer.deserialize(o))
                .collect(Collectors.toList());
            jedis.zremrangeByScore(redisKey, min, max);
            return retList;
        } catch (JedisConnectionException e) {
            logger.error("wtf", e);
            return Collections.emptyList();
        } finally {
            if(null != jedis) {
                jedis.close();
            }
        }
    }




    public void close() {
        pool.close();
    }




}
