package com.hogwartsmini.demo.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="查询task信息列表对象")
@Data
public class QueryHogwartsTestTaskListDto {
    @ApiModelProperty(value="task名字")
    private String name;
    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
