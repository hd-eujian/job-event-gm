package com.eujian.gm.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* @author yeyongjian
* @since 2021-02-04
*/
@Data
public class JobEvent extends Model<JobEvent> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Integer id;

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

    @ApiModelProperty(value = "实际执行时间")
    private Date actExecTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @Override
    protected Serializable pkVal() {
        return this.id;
}
}
