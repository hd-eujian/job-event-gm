package com.eujian.gm.scheduled;

import com.eujian.gm.service.JobEventService;
import com.eujian.gm.support.RedisTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class JobScheduled {

    private boolean isLeader = false;

    private String leaderId = UUID.randomUUID().toString();

    private String leaderKey = "jobEvent:leaderKey";

    private AtomicInteger count = new AtomicInteger();

    @Autowired
    private JobEventService jobEventService;
    @Autowired
    private RedisTemplateService redisTemplateService;
    /**
     * 抢占任务，成为leader
     */
    @Scheduled(cron = " 0/1 * * * * ? ")
    public void beLeader() {
        boolean lock = redisTemplateService.getLock(leaderKey, leaderId, 10, 1);
        if(lock){
            isLeader = true;
            int i = count.incrementAndGet();
            if(i>60){
                log.info("我成为leader");
                count.set(0);
            }
        }
    }

    /**
     * leader工作
     */
    @Scheduled(cron = " 0/1 * * * * ? ")
    public void doJob() {
        if(!isLeader){
            log.debug("不是leader，不工作");
            return;
        }
        jobEventService.checkRedisJob();

    }

    /**
     * 把准备就绪的任务加到redis
     */
    @Scheduled(cron = " 0/1 * * * * ? ")
    public void moveDb2Redis() {
        if(!isLeader){
            log.debug("不是leader，不工作");
            return;
        }
        jobEventService.moveDb2Redis();

    }
}
