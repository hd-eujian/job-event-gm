package com.eujian.gm.controller;

import com.eujian.gm.common.RedisKey;
import com.eujian.gm.entity.creq.RedisJobEventReq;
import com.eujian.gm.entity.creq.RegisterReq;
import com.eujian.gm.entity.redisbo.RedisJobEvent;
import com.eujian.gm.service.JobEventService;
import com.eujian.gm.support.CResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/jobEvent")
@RestController
public class JobEventController {
    @Autowired
    private JobEventService jobEventService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/register")
    public CResult<Object> register(@RequestBody RegisterReq registerReq){
//        Integer id = jobEventService.registerEvent(registerReq);
        return CResult.success(registerReq);
    }

    @PostMapping("/registerDemo")
    public CResult<Integer> registerDemo(@RequestBody RedisJobEventReq req){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(RedisKey.JOB_ZET_KEY, req,req.getScore().doubleValue());
        return CResult.success(1);
    }
}
