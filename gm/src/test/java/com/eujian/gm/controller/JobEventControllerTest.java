package com.eujian.gm.controller;

import com.eujian.gm.BaseTests;
import com.eujian.gm.entity.creq.RegisterReq;
import com.eujian.gm.support.RedisTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JobEventControllerTest extends BaseTests {
    @Autowired
    private JobEventController jobEventController;
    @Autowired
    private RedisTemplateService redisTemplateService;
    @Test
    void register() {
        String value = UUID.randomUUID().toString();
        String key = "lock92Key";
        boolean locak = redisTemplateService.getLock(key, value, 100, 10);
        boolean b = redisTemplateService.releaseLock(key, value);
        System.out.println(locak);
        System.out.println(b);
//        RegisterReq registerReq = new RegisterReq();
//        registerReq.setCallUrl("url");
//        registerReq.setCallJson("json");
//        registerReq.setEventName("name");
//        registerReq.setStatus(1);
//        registerReq.setPlanExecTime(new Date());
//        jobEventController.register(registerReq);

    }
}