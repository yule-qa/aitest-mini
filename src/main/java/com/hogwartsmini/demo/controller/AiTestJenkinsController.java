package com.hogwartsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dto.*;
import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.dto.jenkins.UpdateHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.hogwartsmini.demo.service.AiTestJenkinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@Api(tags = "霍格沃兹测试学院--Jenkins管理")
@RestController
@RequestMapping("jenkins") //接口访问路径
@Slf4j  //java 里的一个日志规范,加上这个注释，就可以使用log.方法名（)了
public class AiTestJenkinsController {

    @Autowired
    private AiTestJenkinsService aiTestJenkinsService;
    @Autowired
    private TokenDb tokenDb;


    @ApiOperation("增加jenkins接口")
    //@RequestMapping(value ="login",method = RequestMethod.POST)
    @PostMapping()    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<HogwartsTestJenkins> save(HttpServletRequest request,  @RequestBody AddHogwartsTestJenkinsDto addHogwartsTestJenkinsDto) throws IOException, URISyntaxException {
        HogwartsTestJenkins hogwartsTestJenkins=new HogwartsTestJenkins();
        //校验Jenkins url 是否为空
        if(StringUtils.isEmpty(addHogwartsTestJenkinsDto.getUrl())){
            return ResultDto.fail("jenkins url不可为空");
        }
        //通过请求，获取token值
        String tokenStr=request.getHeader(UserBaseStr.LOGIN_TOKEN);
        //通过tokenDB（是个map，结构{token:tokenDto}） 查询到token值对应的tokenDto
        TokenDto tokenDto =tokenDb.getUserInfo(tokenStr);

        //将addHogwartsTestJenkinsDto属性，拷贝到hogwartsTestJenkins类里，有个条件，前后两个类中属性名字必须一致
        BeanUtils.copyProperties(addHogwartsTestJenkinsDto,hogwartsTestJenkins);
        //通过Dto里查询到用户信息
        hogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());
        log.info("添加jenkins job的参数为"+ JSONObject.toJSONString(hogwartsTestJenkins));
        return  aiTestJenkinsService.save(tokenDto,hogwartsTestJenkins);
    }

    @ApiOperation("修改jenkins接口")
    @PutMapping()    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<HogwartsTestJenkins> update(HttpServletRequest request,  @RequestBody UpdateHogwartsTestJenkinsDto updateHogwartsTestJenkinsDto) throws IOException, URISyntaxException {
        HogwartsTestJenkins hogwartsTestJenkins=new HogwartsTestJenkins();
        //校验Jenkins url 是否为空
        if(StringUtils.isEmpty(updateHogwartsTestJenkinsDto.getUrl())){
            return ResultDto.fail("jenkins url不可为空");
        }
        //通过请求，获取token值
        String tokenStr=request.getHeader(UserBaseStr.LOGIN_TOKEN);
        //通过tokenDB（是个map，结构{token:tokenDto}） 查询到token值对应的tokenDto
        TokenDto tokenDto =tokenDb.getUserInfo(tokenStr);

        //将addHogwartsTestJenkinsDto属性，拷贝到h-=gwartsTestJenkins类里，有个条件，前后两个类中属性名字必须一致
        BeanUtils.copyProperties(updateHogwartsTestJenkinsDto,hogwartsTestJenkins);
        //通过Dto里查询到用户信息
        hogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());
        log.info("添加jenkins job的参数为"+ JSONObject.toJSONString(hogwartsTestJenkins));
        return  aiTestJenkinsService.update(tokenDto,hogwartsTestJenkins);
    }

    @ApiOperation("删除jenkins接口")
    @DeleteMapping("{jenkinsId}")
    public ResultDto<HogwartsTestJenkins> delete(HttpServletRequest request,@PathVariable("jenkinsId") Integer jenkinsId) throws IOException, URISyntaxException {
        HogwartsTestJenkins hogwartsTestJenkins=new HogwartsTestJenkins();
        //校验Jenkins id 是否为空
        if(null == jenkinsId){
            return ResultDto.fail("jenkinsid不可为空");
        }
        //通过请求，获取token值
        String tokenStr=request.getHeader(UserBaseStr.LOGIN_TOKEN);
        //通过tokenDB（是个map，结构{token:tokenDto}） 查询到token值对应的tokenDto
        TokenDto tokenDto =tokenDb.getUserInfo(tokenStr);
        //通过Dto里查询到用户信息
        hogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

        //创建要查询的对象，为后续操作数据库做准备
        hogwartsTestJenkins.setId(jenkinsId);
        log.info("准备删除job"+ JSONObject.toJSONString(hogwartsTestJenkins));
        return  aiTestJenkinsService.delete(tokenDto,hogwartsTestJenkins);
    }


    @ApiOperation("jenkins分页接口")
    @GetMapping("list")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public ResultDto<PageTableResponse<HogwartsTestJenkins>> list(HttpServletRequest request,
                                                                  PageTableRequest<QueryHogwartsTestJenkinsListDto> pageTableRequest)  {
        //从客户端请求的header中获取token，并根据token获取用户信息

        TokenDto tokenDto=tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        //获取分页请求中的查询参数对象
        //将当前用户id作为查询条件，防止用户数据混乱
        if(Objects.isNull(pageTableRequest.getParams())){
            pageTableRequest.setParams(new QueryHogwartsTestJenkinsListDto());
        }
        pageTableRequest.getParams().setCreateUserId(tokenDto.getUserId());

//        pageTableRequest.setPageNum(Integer.valueOf(pageNum) );
        log.info("向service传递的请求参数"+JSONObject.toJSONString(pageTableRequest));
        return aiTestJenkinsService.list(pageTableRequest);
    }


}
