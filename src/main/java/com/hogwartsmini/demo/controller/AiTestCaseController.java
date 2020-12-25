package com.hogwartsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartsmini.demo.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartsmini.demo.entity.BaseEntityNew;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.service.AiTestCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;

@Api(tags = "霍格沃兹测试学院--测试用例管理")
@RestController
@RequestMapping("testCase") //接口访问路径
@Slf4j  //java 里的一个日志规范,加上这个注释，就可以使用log.方法名（)了
public class AiTestCaseController {

    @Autowired
    private AiTestCaseService aiTestCaseService;
    @Autowired
    private TokenDb tokenDb;

    @ApiOperation("新增文本类型测试用例接口")
    @PostMapping("text")
    public ResultDto saveText(HttpServletRequest request,@RequestBody AddHogwartsTestCaseDto addHogwartsTestCaseDto) throws IOException, URISyntaxException {
        log.info("=====新增文本测试用例-请求入参====："+ JSONObject.toJSONString(addHogwartsTestCaseDto));
        if(Objects.isNull(addHogwartsTestCaseDto)){
            return ResultDto.fail("请求参数不能为空");
        }
        if(StringUtils.isEmpty(addHogwartsTestCaseDto.getCaseData())){
            return ResultDto.fail("测试用例数据不能为空");
        }
        if(StringUtils.isEmpty(addHogwartsTestCaseDto.getCaseName())){
            return ResultDto.fail("测试用例名称不能为空");
        }
        HogwartsTestCase hogwartsTestCase=new HogwartsTestCase();
        BeanUtils.copyProperties(addHogwartsTestCaseDto,hogwartsTestCase);
        TokenDto  tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        hogwartsTestCase.setCreateUserId(tokenDto.getUserId());
        return aiTestCaseService.save(tokenDto,hogwartsTestCase);
    }

    @ApiOperation("新增文件类型测试用例接口")
    //@RequestMapping(value ="login",method = RequestMethod.POST)
    @PostMapping("file")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto saveFile(HttpServletRequest request
                            , @RequestParam("caseFile") MultipartFile caseFile
                            , AddHogwartsTestCaseDto addHogwartsTestCaseDto) throws IOException, URISyntaxException {
        //参数校验
        if(Objects.isNull(addHogwartsTestCaseDto)){
               return ResultDto.fail("参数不为空");
           }
        //获取文件流中的内容
            InputStream inputStream=caseFile.getInputStream();
            String caseData = IOUtils.toString(inputStream);
            log.info("caseData=="+caseData);
            addHogwartsTestCaseDto.setCaseData(caseData);
            TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
            HogwartsTestCase hogwartsTestCase =new HogwartsTestCase();
            BeanUtils.copyProperties(addHogwartsTestCaseDto,hogwartsTestCase);
            hogwartsTestCase.setCreateUserId(tokenDto.getUserId());

            return  aiTestCaseService.save(tokenDto,hogwartsTestCase);
    }

    @ApiOperation("查询测试用例列表分页接口")
    @GetMapping("list")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<PageTableResponse<HogwartsTestCase>> list(HttpServletRequest request,
                                                               PageTableRequest<QueryHogwartsTestCaseListDto> pageTableRequest) throws IOException, URISyntaxException {

        //从客户端请求的header中获取token，并根据token获取用户信息
        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        //获取分页请求中的查询参数对象
        //讲当前用户id作为查询条件，防止用户数据混乱
        if(Objects.isNull(pageTableRequest.getParams())){
            pageTableRequest.setParams(new QueryHogwartsTestCaseListDto());
        }
        pageTableRequest.getParams().setCreateUserId(tokenDto.getUserId());

        log.info("向service传递的请求参数"+JSONObject.toJSONString(pageTableRequest));
        return aiTestCaseService.list(pageTableRequest);
    }
    @ApiOperation("删除测试用例接口")
    @DeleteMapping("{caseId}")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<HogwartsTestCase> delete(HttpServletRequest request,@PathVariable("caseId") Integer caseId) {
        HogwartsTestCase hogwartsTestCase=new HogwartsTestCase();
        //从客户端请求的header中获取token，并根据token获取用户信息
        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        hogwartsTestCase.setId(caseId);
        hogwartsTestCase.setCreateUserId(tokenDto.getUserId());
        log.info("准备删除case"+ JSONObject.toJSONString(hogwartsTestCase));
        return aiTestCaseService.delete(hogwartsTestCase);
    }

}
