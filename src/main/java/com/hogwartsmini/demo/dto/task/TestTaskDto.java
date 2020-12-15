package com.hogwartsmini.demo.dto.task;

import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ApiModel(value = "任务类dto",description = "请求类")
@Data
public class TestTaskDto {

    @ApiModelProperty(value="测试任务信息",required=true)
    private AddHogwartsTestTaskDto testTask;

    /**
     * 名称
     */
    @ApiModelProperty(value="测试用例的id列表",required=true)
    private List<Integer> caseIdList;

}
