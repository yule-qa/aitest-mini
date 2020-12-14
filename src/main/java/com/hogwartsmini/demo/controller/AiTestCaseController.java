package com.hogwartsmini.demo.controller;

import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartsmini.demo.service.AiTestCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
@RequestMapping("testcase") //接口访问路径
@Slf4j  //java 里的一个日志规范,加上这个注释，就可以使用log.方法名（)了
public class AiTestCaseController {

    @Autowired
    private AiTestCaseService aiTestCaseService;
    @Autowired
    private TokenDb tokenDb;


    @ApiOperation("增加文件上传接口")
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
            return ResultDto.success("成功",caseData);
    }



}
