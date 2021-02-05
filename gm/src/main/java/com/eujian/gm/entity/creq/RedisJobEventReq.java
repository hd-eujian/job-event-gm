package com.eujian.gm.entity.creq;

import lombok.Data;

import java.io.Serializable;

@Data
public class RedisJobEventReq implements Serializable {

    private Integer id;

    private Long score;
}
