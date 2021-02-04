package com.eujian.gm.entity.creq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RegisterReq implements Serializable {


    @ApiModelProperty(value = "事件名称")
    private String eventName;

    @ApiModelProperty(value = "回调url")
    private String callUrl;

    @ApiModelProperty(value = "回调消息体")
    private String callJson;

    @ApiModelProperty(value = "事件状态")
    private Integer status;

    @ApiModelProperty(value = "计划机制形式")
    private Date planExecTime;

}
