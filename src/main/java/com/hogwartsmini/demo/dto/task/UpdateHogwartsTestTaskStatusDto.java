package com.hogwartsmini.demo.dto.task;

import com.hogwartsmini.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="修改任务状态对象")
@Data
public class UpdateHogwartsTestTaskStatusDto extends BaseEntityNew{
    /**
     * ID
     */
    @ApiModelProperty(value="任务主键",required=true)
    private Integer taskId;

    /**
     *构建地址
     */
    @ApiModelProperty(value="构建地址",required=true)
    private String buildUrl;

    /**
     *任务状态码
     */
    @ApiModelProperty(value="任务状态码",required=true)
    private Integer status;

}
