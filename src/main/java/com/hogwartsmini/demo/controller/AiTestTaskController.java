package com.hogwartsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dto.RequestInfoDto;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.task.AddHogwartsTestTaskDto;
import com.hogwartsmini.demo.dto.task.StartTestDto;
import com.hogwartsmini.demo.dto.task.TestTaskDto;
import com.hogwartsmini.demo.dto.task.UpdateHogwartsTestTaskStatusDto;
import com.hogwartsmini.demo.entity.HogwartsTestTask;
import com.hogwartsmini.demo.service.AiTestTaskService;
import com.hogwartsmini.demo.util.AddressUtils;
import com.hogwartsmini.demo.util.StrUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    @PostMapping()    // 表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto save(HttpServletRequest request
                            , @RequestBody  TestTaskDto taskDto) throws IOException, URISyntaxException {
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
        if(StringUtils.isEmpty(testTask.getName())) {
            testTask.setName("系统默认");
        }
        testTask.setCreateUserId(tokenDto.getUserId());
        testTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        //调用service
        return aiTestTaskService.save(taskDto,UserBaseStr.Task_Type_One);

    }
    @ApiOperation("执行测试任务接口")
    @PostMapping("start")    // 表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto startTask(HttpServletRequest request
            , @RequestBody StartTestDto startTaskDto) throws IOException, URISyntaxException {
        //参数校验
        if(Objects.isNull(startTaskDto)){
            return ResultDto.fail("参数不能为空");
        }
        if(Objects.isNull(startTaskDto.getTaskId())) {
            return ResultDto.fail("任务id不能为空");
        }
        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        //获取当前登录用户信息
        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserId());
        hogwartsTestTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());
        hogwartsTestTask.setId(startTaskDto.getTaskId());
        hogwartsTestTask.setTestCommand(startTaskDto.getTestCommand());

        //获取下方请求url,
        String requestUrl=request.getRequestURL().toString();
        String localip=AddressUtils.getIps(requestUrl).get(0); // 127.0.0.1
        String innerip= AddressUtils.getInnetIp();  //内网ip
//        家里环境用外网
//        String innerip= AddressUtils.getV4IP();  //外网ip
        String url =requestUrl.replace(localip,innerip);
        log.info("jenkins 执行任务接口，jenkinsapi请求地址==="+url);
        String  baseurl= StrUtils.getHostAndPort(url);

        //用于存储后端服务baseUrl、token等数据
        RequestInfoDto requestInfoDto=new RequestInfoDto();
        requestInfoDto.setBaseUrl(baseurl);
        requestInfoDto.setRequestUrl(url);
        requestInfoDto.setToken(tokenDto.getToken());

        log.info("requestInfoDto=="+ JSONObject.toJSONString(requestInfoDto));

        //调用service
        return aiTestTaskService.startTask(tokenDto,requestInfoDto,hogwartsTestTask);

    }

    @ApiOperation("任务列表接口")
    @GetMapping("list")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(HttpServletRequest request)  {
        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        //从客户端请求的header中获取token，并根据token获取用户信息
        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserId());
        log.info("向service传递的请求参数"+JSONObject.toJSONString(hogwartsTestTask));
        return aiTestTaskService.list(hogwartsTestTask);
    }

    @ApiOperation("修改任务执行状态接口")
    @PutMapping("status")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<HogwartsTestTask>  updateStatus(HttpServletRequest request , @RequestBody UpdateHogwartsTestTaskStatusDto updateHogwartsTestTaskStatusDto)  {
        log.info("修改测试任务状态-入参"+JSONObject.toJSONString(updateHogwartsTestTaskStatusDto));
        //判断参数为空
        if(Objects.isNull(updateHogwartsTestTaskStatusDto)){
            return ResultDto.fail("修改测试信息不能为空");
        }
        Integer taskId=updateHogwartsTestTaskStatusDto.getTaskId();
        String buildUrl=updateHogwartsTestTaskStatusDto.getBuildUrl();
        Integer status=updateHogwartsTestTaskStatusDto.getStatus();

        if(Objects.isNull(taskId)){
            return ResultDto.fail("测试任务id不能为空");
        }
        if(StringUtils.isEmpty(buildUrl)){
            return ResultDto.fail("jenkins构建地址不能为空");
        }
        if(Objects.isNull(status)){
            return ResultDto.fail("任务状态不能为空");
        }
        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        hogwartsTestTask.setId(taskId);
        hogwartsTestTask.setBuildUrl(buildUrl);
        hogwartsTestTask.setStatus(status);

        //获取Token
        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        hogwartsTestTask.setCreateUserId(tokenDto.getUserId());
        return aiTestTaskService.updateStatus(hogwartsTestTask);
    }


}
