package com.hogwartsmini.demo.controller;

import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.task.AddHogwartsTestTaskDto;
import com.hogwartsmini.demo.dto.task.TestTaskDto;
import com.hogwartsmini.demo.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.service.AiTestCaseService;
import com.hogwartsmini.demo.service.AiTestTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Api(tags = "霍格沃兹测试学院--测试任务管理")
@RestController
@RequestMapping("task") //接口访问路径
@Slf4j  //java 里的一个日志规范,加上这个注释，就可以使用log.方法名（)了
public class AiTestTaskController {

    @Autowired
    private AiTestTaskService aiTestTaskService;
    @Autowired
    private TokenDb tokenDb;


    @ApiOperation("添加测试任务接口")
    @PostMapping("file")    // 表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto saveFile(HttpServletRequest request
                            , TestTaskDto taskDto) throws IOException, URISyntaxException {
        //参数校验
        List<Integer> caseIdList=taskDto.getCaseIdList();
        if(Objects.isNull(caseIdList) || caseIdList.size()==0){
            return ResultDto.fail("测试用例不能为空");
        }
        //从tokenDb中获取TokenDto
        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        AddHogwartsTestTaskDto testTask=taskDto.getTestTask();
        if(Objects.isNull(testTask)){
            testTask =new AddHogwartsTestTaskDto();
            taskDto.setTestTask(testTask);
        }
        testTask.setName("系统默认");
        testTask.setCreateUserId(tokenDto.getUserId());
        testTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        //调用service
        aiTestTaskService.save(taskDto,UserBaseStr.Task_Type_One);

        return ResultDto.success("成功");
    }



}
