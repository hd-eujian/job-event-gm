package com.eujian.gm.service;

import com.eujian.gm.BaseTests;
import com.eujian.gm.common.RedisKey;
import com.eujian.gm.entity.redisbo.RedisJobEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JobEventServiceTest extends BaseTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void checkRedisJob() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(RedisKey.JOB_ZET_KEY,1,1);
//        zSetOperations.add(RedisKey.JOB_ZET_KEY,2,2);
//        Set<RedisJobEvent> set = zSetOperations.range(RedisKey.JOB_ZET_KEY, 0,1);
//        ArrayList<RedisJobEvent> objects = new ArrayList<>(set);
//        RedisJobEvent redisJobEvent = objects.get(0);
//        double score = redisJobEvent.getScore();
        Long time = new Date().getTime()/1000L;
        Double v = time.doubleValue();
        long l = v.longValue();
        System.out.println(v);
        System.out.println(l);
    }
}