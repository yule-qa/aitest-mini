package com.hogwartsmini.demo.service.Impl;


import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.dao.*;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.task.AddHogwartsTestTaskDto;
import com.hogwartsmini.demo.dto.task.TestTaskDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.hogwartsmini.demo.entity.HogwartsTestTask;
import com.hogwartsmini.demo.service.AiTestTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class AiTestTaskServiceImpl implements AiTestTaskService {

    @Autowired
    private HogwartsTestTaskMapper hogwartsTestTaskMapper;
    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;
    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;
    @Autowired
    private HogwartsTestTaskCaseRelMapper hogwartsTestTaskCaseRelMapper;

    @Autowired
    private TokenDb tokenDb;

    /**
     * 添加jenkins
     * @param taskDto
     * @return
     */
    @Override
    public ResultDto save(TestTaskDto taskDto, Integer taskListId) throws IOException, URISyntaxException {
        //todo
        AddHogwartsTestTaskDto taskTest=taskDto.getTestTask();
        List<Integer> caseIdList = taskDto.getCaseIdList();

        //再次校验testTask,caseIdList 是否为空
        Integer createUserId=taskTest.getCreateUserId();
        Integer testJenkinsId=taskTest.getTestJenkinsId();

        //校验testJenkinsId是否为空校验

        if(Objects.isNull(testJenkinsId)){
            return  ResultDto.fail("用户默认Jenkinsid为空");
        }

        //根据用户默认JenkinsId查询Jenkins信息并做非空
        HogwartsTestJenkins queryHogwartsTestJenkins=new HogwartsTestJenkins();
        queryHogwartsTestJenkins.setCreateUserId(createUserId);
        queryHogwartsTestJenkins.setId(testJenkinsId);
        HogwartsTestJenkins resultHogwartsTestJenkins=hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);
        if (Objects.isNull(resultHogwartsTestJenkins)){
            return ResultDto.fail("用户默认Jenkins信息为空");

        //查询测试用例，如id为25,26,28
        List<HogwartsTestCase> hogwartsTestCases=hogwartsTestCaseMapper.selectByIds("25,26,28");
        }
        return ResultDto.success("成功");
    }


    @Override
    public ResultDto<HogwartsTestTask> delete(Integer taskId, Integer createUserId) {
        return null;
    }

    @Override
    public ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask) {
        return null;
    }

    @Override
    public ResultDto<HogwartsTestTask> getById(Integer taskId, Integer createUserId) {
        return null;
    }

//    @Override
//    public ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest) {
//        return null;
//    }
//
//    @Override
//    public ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask) throws IOException {
//        return null;
//    }

    @Override
    public ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask) {
        return null;
    }


}
