package com.eujian.gm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eujian.gm.common.RedisKey;
import com.eujian.gm.entity.JobEvent;
import com.eujian.gm.entity.creq.RegisterReq;
import com.eujian.gm.entity.redisbo.RedisJobEvent;
import com.eujian.gm.mapper.JobEventMapper;
import com.eujian.gm.support.BeanCopyUtils;
import com.eujian.gm.support.PublicUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.EntityWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        jobEvent.setStatus(1);
        jobEventMapper.insert(jobEvent);
        return jobEvent.getId();
    }

    public void checkRedisJob() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Set<Integer> set = null;
        while (true){
            set = zSetOperations.range(RedisKey.JOB_ZET_KEY, 0,1);

            if(PublicUtil.isEmpty(set)){
                log.debug("没有任务需要执行");
                return;
            }
            ArrayList<Integer> objects = new ArrayList(set);
            Integer event = objects.get(0);
            Double score = zSetOperations.score(RedisKey.JOB_ZET_KEY, event);
            long scoreValue = score.longValue();
            long time = new Date().getTime();
            if(scoreValue>time){
                return;
            }
            log.info("开始执行任务，event={}",event);
            this.doEvent(event);
            zSetOperations.remove(RedisKey.JOB_ZET_KEY,event);
        }



    }

    private void doEvent(Integer eventId) {
        try {
            JobEvent jobEvent = jobEventMapper.selectById(eventId);
            if(PublicUtil.isEmpty(jobEvent)){
                log.error("找不到jobEvent,id={}", eventId);
                return;
            }
            if(!PublicUtil.isEquals(jobEvent.getStatus(),1)){
                log.error("不是未执行的jobEvent,id={}", eventId);
                return;
            }
            jobEvent.setStatus(2);
            jobEvent.setPlanExecTime(new Date());
            jobEventMapper.updateById(jobEvent);
        }catch (Exception e){
            log.error("ex={}",e.getMessage(),e);
        }



    }

    public void moveDb2Redis() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        QueryWrapper<JobEvent> wrapper = new QueryWrapper();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = now.plusDays(-30);
        LocalDateTime end = now.plusMinutes(5);
        wrapper.between("plan_exec_time",begin,end);
        wrapper.eq("status",1);
        List<JobEvent> jobEvents = jobEventMapper.selectList(wrapper);
        if(PublicUtil.isEmpty(jobEvents)){
            log.info("没有任务需要添加到redis");
            return;
        }
        jobEvents.forEach(j->{
//            RedisJobEvent event = new RedisJobEvent();
//            event.setId(j.getId());
            long time = j.getPlanExecTime().getTime();
            zSetOperations.add(RedisKey.JOB_ZET_KEY,j.getId(),time);
            log.info("添加任务到redis成功,id={}",j.getId());
        });

    }
}
