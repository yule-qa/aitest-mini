package com.hogwartsmini.demo.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.ServiceException;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dao.*;
import com.hogwartsmini.demo.dto.RequestInfoDto;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartsmini.demo.dto.task.AddHogwartsTestTaskDto;
import com.hogwartsmini.demo.dto.task.TestTaskDto;
import com.hogwartsmini.demo.entity.*;
import com.hogwartsmini.demo.service.AiTestTaskService;
import com.hogwartsmini.demo.util.JenkinsUtil;
import com.hogwartsmini.demo.util.StrUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


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
    @Transactional(rollbackFor = Exception.class)  //因为一个方法里操作了多个表，所以添加事务回滚（出现问题时，将刚刚添加的数据库信息全部清理）
    @Override
    public ResultDto save(TestTaskDto taskDto, Integer taskType) throws IOException, URISyntaxException {
        StringBuilder testCommand=new StringBuilder();
        AddHogwartsTestTaskDto testTask=taskDto.getTestTask();
        List<Integer> caseIdList = taskDto.getCaseIdList();

        //再次校验testTask,caseIdList 是否为空
        Integer createUserId=testTask.getCreateUserId();
        Integer testJenkinsId=testTask.getTestJenkinsId();

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
        }

        //根据用户选择的测试用例id，查询测试用例信息，如id为25,26,28
        List<HogwartsTestCase> hogwartsTestCasesList=hogwartsTestCaseMapper.selectByIds(StrUtils.list2String(caseIdList));
        //根据以上准好的测试数据，开始生成测试命令
        makeTestCommand(testCommand,resultHogwartsTestJenkins,hogwartsTestCasesList);
        //准备测试任务对象，并存库
        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        hogwartsTestTask.setName(testTask.getName());
        hogwartsTestTask.setTestJenkinsId(testTask.getTestJenkinsId());
        hogwartsTestTask.setCreateUserId(testTask.getCreateUserId());
        hogwartsTestTask.setRemark(testTask.getRemark());

        hogwartsTestTask.setTaskType(taskType);
        hogwartsTestTask.setTestCommand(testCommand.toString());
        hogwartsTestTask.setCaseCount(caseIdList.size());
        hogwartsTestTask.setStatus(UserBaseStr.STATUS_ONE);
        hogwartsTestTask.setCreateTime(new Date());
        hogwartsTestTask.setUpdateTime(new Date());

        hogwartsTestTaskMapper.insert(hogwartsTestTask);
        //测试任务详情数据存库
        List<HogwartsTestTaskCaseRel> hogwartsTestTaskCaseRelsList=new ArrayList<>();
        for(Integer caseId:caseIdList){
            HogwartsTestTaskCaseRel hogwartsTestTaskCaseRel=new HogwartsTestTaskCaseRel();
            hogwartsTestTaskCaseRel.setTaskId(hogwartsTestTask.getId());
            hogwartsTestTaskCaseRel.setCaseId(caseId);
            hogwartsTestTaskCaseRel.setCreateUserId(createUserId);
            hogwartsTestTaskCaseRel.setCreateTime(new Date());
            hogwartsTestTaskCaseRel.setUpdateTime(new Date());
            hogwartsTestTaskCaseRelsList.add(hogwartsTestTaskCaseRel);
        }
        hogwartsTestTaskCaseRelMapper.insertList(hogwartsTestTaskCaseRelsList);

        return ResultDto.success("成功",hogwartsTestTask);
    }

    /**
     * 生成测试用例命令
      * @param testCommand
     * @param resultHogwartsTestJenkins
     * @param hogwartsTestCasesList
     */
    private void makeTestCommand(StringBuilder testCommand, HogwartsTestJenkins resultHogwartsTestJenkins, List<HogwartsTestCase> hogwartsTestCasesList) {

    //校验Jenkins是否为空
        if(Objects.isNull(resultHogwartsTestJenkins)){
            throw new ServiceException("生成命令时Jenkins信息为空");
        }
        if(Objects.isNull(hogwartsTestCasesList) || hogwartsTestCasesList.size() ==0){ //用常量.equals（变量）可以提高系统健壮性，防止当变量为空是，抛异常
            throw new ServiceException("生成命令时测试用例列表信息为空");
        }
    //获取commandRunCaseType 、 testCommrd
    //命令运行的测试用例类型 1 文本 2 文件
        Integer commandRunCaseType = resultHogwartsTestJenkins.getCommandRunCaseType().intValue();
    //具体测试工具的运行命令。如mvn test、 pytest、 httprunner
        String testCommandStr=resultHogwartsTestJenkins.getTestCommand();
    //文本类型为空，则设置默认类型为文本
        if(Objects.isNull(commandRunCaseType)){
            commandRunCaseType=1;
        }
    //文本类型处理方式
        if(UserBaseStr.CASE_TYPE_ONE.equals(commandRunCaseType)){ //用常量.equals（变量）可以提高系统健壮性，防止当变量为空是，抛异常
            //生成文本类型的测试命令，刑如
            //mvn test 包1
            //mvn test 包2
            //mvn test 包3
            for(HogwartsTestCase hogwartsTestCase:hogwartsTestCasesList){
                //拼装命令前缀
                testCommand.append(resultHogwartsTestJenkins.getTestCommand()).append("  ");
                //拼装测试数据
                testCommand.append(hogwartsTestCase.getCaseData());
                testCommand.append("\n");
            }
        }

    //文件类型
        if(UserBaseStr.CASE_TYPE_TWO.equals(commandRunCaseType)){ //用常量.equals（变量）可以提高系统健壮性，防止当变量为空是，抛异常
            //校验测试用例后缀名
            if(StringUtils.isEmpty(resultHogwartsTestJenkins.getCommandRunCaseSuffix())){
                throw new ServiceException("测试用例后缀名为空，此项为必填项");
            }

            for (HogwartsTestCase hogwartsTestCase:hogwartsTestCasesList){
                //拼装curl命令-下载测试用例数据
                makeCurlCommand(testCommand,hogwartsTestCase,resultHogwartsTestJenkins.getCommandRunCaseSuffix());
                //拼装测试命令
                testCommand.append("\n");
                testCommand.append(resultHogwartsTestJenkins.getTestCommand()).append("  ");
                testCommand.append(hogwartsTestCase.getCaseName()).append(".").append(resultHogwartsTestJenkins.getCommandRunCaseSuffix());
                testCommand.append(" || true");
                testCommand.append("\n");
            }


        }
        testCommand.append("\n");
    }

    private void makeCurlCommand(StringBuilder testCommand, HogwartsTestCase hogwartsTestCase, String commandRunCaseSuffix) {
        testCommand.append("curl -o ");
        testCommand.append(hogwartsTestCase.getCaseName())
                .append(".")
                .append(commandRunCaseSuffix);
        testCommand.append(" ");
        testCommand.append("${aitestBaseUrl}")
                .append("/testCase/data/").append(hogwartsTestCase.getId());
        testCommand.append("  ");
        testCommand.append("-H \"token: ${token}\"");
        testCommand.append(" || true");
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

    @Override
    public ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask) throws IOException, URISyntaxException {
        //参数校验和默认Jenkins是否有效
        Integer defaultJenkinsId=tokenDto.getDefaultJenkinsId();
        if(Objects.isNull(defaultJenkinsId)){
            return ResultDto.fail("默认JenkinsId为空 ");
        }
        HogwartsTestJenkins queryHogwartsTestJenkins=new HogwartsTestJenkins();
        queryHogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());
        queryHogwartsTestJenkins.setId(defaultJenkinsId);

        HogwartsTestJenkins requestHogwartsTestJenkins=hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);
        if(Objects.isNull(requestHogwartsTestJenkins)){
            return ResultDto.fail("默认jenkins信息为空");
        }
        //获取用户信息
        HogwartsTestUser queryHogwartsTestUser=new HogwartsTestUser();
        queryHogwartsTestUser.setId(tokenDto.getUserId());
        HogwartsTestUser resultHogwartsTestUser=hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);
        //根据任务ID查询测试任务
        HogwartsTestTask queryHogwartsTestTask=new HogwartsTestTask();
        queryHogwartsTestTask.setId(hogwartsTestTask.getId());


        HogwartsTestTask resultHogwartsTestTask =hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);
        if(Objects.isNull(resultHogwartsTestTask)){
            return ResultDto.fail("任务信息不存在");
        }


        //获取测试命令并更新任务状态为执行中
        String testCommand =resultHogwartsTestTask.getTestCommand();
        resultHogwartsTestTask.setStatus(UserBaseStr.STATUS_TWO); // 设置状态为执行中
        hogwartsTestTaskMapper.updateByPrimaryKeySelective(resultHogwartsTestTask);

        //获取更新任务状态的回调地址updateStatusUrl
        StringBuilder updateStatusUrlCommand = JenkinsUtil.getUpdateTaskStatusUrl(requestInfoDto,resultHogwartsTestTask);
        String updateStatusUrl=updateStatusUrlCommand.toString();

        //组装Jenkins构建参数
        Map<String,String> map=new HashMap<>();
        map.put("aitestBaseUrl",requestInfoDto.getBaseUrl());
        map.put("token",requestInfoDto.getBaseUrl());
        map.put("testCommand",testCommand);
        map.put("updateStatusData",updateStatusUrl);
        // 调用Jenkins
        OperateJenkinsJobDto operateJenkinsJobDto=new OperateJenkinsJobDto();
        operateJenkinsJobDto.setTokenDto(tokenDto);
        operateJenkinsJobDto.setHogwartsTestJenkins(requestHogwartsTestJenkins);
        operateJenkinsJobDto.setParams(map);
        operateJenkinsJobDto.setHogwartsTestUser(resultHogwartsTestUser);
        ResultDto<HogwartsTestUser> resultDto=JenkinsUtil.build(operateJenkinsJobDto);

        return ResultDto.success("成功", resultHogwartsTestTask);
    }

    @Override
    public ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask) {
        return null;
    }


}
