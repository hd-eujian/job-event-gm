package com.eujian.gm.controller;

import com.eujian.gm.entity.creq.RegisterReq;
import com.eujian.gm.service.JobEventService;
import com.eujian.gm.support.CResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/jobEvent")
@RestController
public class JobEventController {
    @Autowired
    private JobEventService jobEventService;
    @PostMapping("/register")
    public CResult<Integer> register(@RequestBody RegisterReq registerReq){
        Integer id = jobEventService.registerEvent(registerReq);
        return CResult.success(id);
    }
}
