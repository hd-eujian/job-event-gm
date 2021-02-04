package com.eujian.gm;

import com.eujian.gm.entity.JobEvent;
import com.eujian.gm.mapper.JobEventMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class GmApplicationTests {

    @Autowired
    private JobEventMapper jobEventMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("key","key");
        Object key = redisTemplate.opsForValue().get("key");
        System.out.println(key);
//        JobEvent jobEvent = jobEventMapper.selectById(1);
//        System.out.println(jobEvent);
    }

}
