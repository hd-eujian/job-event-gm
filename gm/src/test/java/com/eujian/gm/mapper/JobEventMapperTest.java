package com.eujian.gm.mapper;

import com.eujian.gm.BaseTests;
import com.eujian.gm.entity.JobEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class JobEventMapperTest extends BaseTests {

    @Autowired
    private JobEventMapper jobEventMapper;

    @Test
    public void tt(){
        JobEvent jobEvent = jobEventMapper.selectById(2);
        System.out.println(jobEvent);
    }

}