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
    @Autowired
    private JobEventService jobEventService;
    @Test
    void checkRedisJob() {
        jobEventService.checkRedisJob();
    }

    @Test
    void registerEvent() {
        jobEventService.registerEvent(null);
    }


    @Test
    void moveDb2Redis() {
        jobEventService.moveDb2Redis();
    }
}