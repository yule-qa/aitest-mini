package com.hogwartsmini.demo.dto.testcase;

import com.hogwartsmini.demo.common.BaseDto;
import com.hogwartsmini.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="查询测试用例case信息列表对象")
@Data
public class QueryHogwartsTestCaseListDto extends BaseDto {
    @ApiModelProperty(value="case名称")
    private String name;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
