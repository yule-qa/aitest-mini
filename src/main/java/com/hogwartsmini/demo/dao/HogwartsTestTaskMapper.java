package com.hogwartsmini.demo.dao;

import com.hogwartsmini.demo.common.MySqlExtensionMapper;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.dto.task.QueryHogwartsTestTaskListDto;
import com.hogwartsmini.demo.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.hogwartsmini.demo.entity.HogwartsTestTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestTaskMapper extends MySqlExtensionMapper<HogwartsTestTask> {
//    List<HogwartsTestTask> list(@Param("params") QueryHogwartsTestTaskListDto queryHogwartsTestCaseListDto);
    List<HogwartsTestTask> list(@Param("params") HogwartsTestTask hogwartsTestTask);
}