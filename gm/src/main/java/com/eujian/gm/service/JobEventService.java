package com.eujian.gm.service;

import com.eujian.gm.entity.JobEvent;
import com.eujian.gm.entity.creq.RegisterReq;
import com.eujian.gm.mapper.JobEventMapper;
import com.eujian.gm.support.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JobEventService {
    @Autowired
    private JobEventMapper jobEventMapper;

    public Integer registerEvent(RegisterReq registerReq) {
        JobEvent jobEvent = BeanCopyUtils.copyProperties(JobEvent.class, registerReq);
        jobEvent.setCreateTime(new Date());
        jobEventMapper.insert(jobEvent);
        return jobEvent.getId();
    }
}
