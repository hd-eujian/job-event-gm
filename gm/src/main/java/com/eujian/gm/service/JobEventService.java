package com.eujian.gm.service;

import com.eujian.gm.common.RedisKey;
import com.eujian.gm.entity.JobEvent;
import com.eujian.gm.entity.creq.RegisterReq;
import com.eujian.gm.entity.redisbo.RedisJobEvent;
import com.eujian.gm.mapper.JobEventMapper;
import com.eujian.gm.support.BeanCopyUtils;
import com.eujian.gm.support.PublicUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
@Slf4j
@Service
public class JobEventService {
    @Autowired
    private JobEventMapper jobEventMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public Integer registerEvent(RegisterReq registerReq) {
        JobEvent jobEvent = BeanCopyUtils.copyProperties(JobEvent.class, registerReq);
        jobEvent.setCreateTime(new Date());
        jobEventMapper.insert(jobEvent);
        return jobEvent.getId();
    }

    public void checkRedisJob() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Set<RedisJobEvent> set = null;
        while (true){
            set = zSetOperations.range(RedisKey.JOB_ZET_KEY, 0,1);
            if(PublicUtil.isEmpty(set)){
                log.debug("没有任务需要执行");
                return;
            }
            ArrayList<RedisJobEvent> objects = new ArrayList<>(set);
            RedisJobEvent event = objects.get(0);
            long scoreValue = event.getScore();
            long time = new Date().getTime();
            if(scoreValue>time){
                return;
            }
            log.info("开始执行任务，event={}",event);
            zSetOperations.remove(RedisKey.JOB_ZET_KEY,event);
        }



    }
}
