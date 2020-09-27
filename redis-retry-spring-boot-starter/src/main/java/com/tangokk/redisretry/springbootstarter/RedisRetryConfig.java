package com.tangokk.redisretry.springbootstarter;

import com.tangokk.redisretry.lib.RetryService;
import com.tangokk.redisretry.lib.impl.RedisRetryDataDao;
import com.tangokk.redisretry.lib.impl.SimpleRetryApi;
import com.tangokk.redisretry.lib.serialize.GenericJackson2JsonRedisSerializer;
import com.tangokk.redisretry.lib.serialize.RedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisRetryConfig {


    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "redis-retry.redis", value = {"host", "port", "password", "database", "key"})
    public RedisRetryDataDao redisRetryDataDao(RedisProperties redisProperties) {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(),
            Protocol.DEFAULT_TIMEOUT, redisProperties.getPassword());

        RedisSerializer<?> serializer = new GenericJackson2JsonRedisSerializer();
        RedisRetryDataDao dao = new RedisRetryDataDao(redisProperties.getDatabase(), redisProperties.getKey(), jedisPool, serializer);
        return dao;
    }

    @Bean
    @ConditionalOnMissingBean(SimpleRetryApi.class)
    @ConditionalOnBean(value = {RedisRetryDataDao.class, RetryService.class})
    public SimpleRetryApi simpleRetryApi(RedisRetryDataDao redisRetryDataDao, @Autowired(required = false) RetryService retryService) {
        SimpleRetryApi retryApi = new SimpleRetryApi(redisRetryDataDao);
        retryApi.setRetryService(retryService);
        return retryApi;
    }

}
