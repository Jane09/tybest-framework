package com.tybest.seckill.config;

import com.tybest.seckill.lock.redisson.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tb
 * @date 2018/12/5 10:15
 */
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperty.class)
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedissonProperty redissonProperty;


//    @Bean
//    @ConditionalOnProperty(name = "redisson.master-slave")
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        SentinelServersConfig ssc = config
//                .useSentinelServers()
//                .addSentinelAddress(redissonProperty.getSentinelAddresses())
//                .setMasterName(redissonProperty.getMasterName())
//                .setTimeout(redissonProperty.getTimeout())
//                .setMasterConnectionPoolSize(redissonProperty.getMasterConnectionPoolSize())
//                .setSlaveConnectionPoolSize(redissonProperty.getSlaveConnectionPoolSize());
//        if(StringUtils.isNotBlank(redissonProperty.getPassword())){
//            ssc.setPassword(redissonProperty.getPassword());
//        }
//        return Redisson.create(config);
//    }

    /**
     * 单机自动装载
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.address")
    public RedissonClient redisClient() {
        Config config = new Config();
        SingleServerConfig ssc = config
                .useSingleServer()
                .setAddress(redissonProperty.getAddress())
                .setTimeout(redissonProperty.getTimeout())
                .setConnectionPoolSize(redissonProperty.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperty.getConnectionMinimumIdleSize());
        if(StringUtils.isNotBlank(redissonProperty.getPassword())){
            ssc.setPassword(redissonProperty.getPassword());
        }
        return Redisson.create(config);
    }


    @Bean
    public RedissonLock redissonLock(RedissonClient redissonClient){
        RedissonLock redissonLock = new RedissonLock();
        redissonLock.setRedissonClient(redissonClient);
        return redissonLock;
    }
}
