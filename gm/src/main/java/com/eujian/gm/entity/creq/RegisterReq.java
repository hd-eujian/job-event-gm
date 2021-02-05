package com.eujian.gm.entity.creq;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty(value = "计划机制形式")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planExecTime;

}
