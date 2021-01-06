package com.hogwartsmini.demo.controller;

import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.UserDto;
import com.hogwartsmini.demo.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * 学习springboot 接收各种http请求
 */
@RestController
@RequestMapping("test")
public class HttpTestController {

    /**
     * GET
     * 请求路径带参形式
     * //http://localhost:8080/test/get?name=xxx&pwd=yyy
     * @return
     */
    @GetMapping("get1")
    public String getTest1(String name,String pwd){
        System.out.println(name);
        System.out.println(pwd);
        return "成功";
    }
    @GetMapping("get2")
    public String getTest2(UserDto user){
        System.out.println(user.getUserName());
        System.out.println(user.getPassword());
        return "成功";
    }
    @GetMapping("get3")
    public String getTest3(HttpServletRequest request){
        System.out.println(request.getParameter("userName"));
        System.out.println(request.getParameter("password"));
        return "成功";
    }
    @GetMapping("get4")
    public String getTest4(@RequestParam("userName") String userName,@RequestParam("password") String pwd){
            System.out.println(userName);
            System.out.println(pwd);
            return "成功";
    }

    /**
     * GET
     * 请求路径作为参数
     * http://localhost:8080/0919/test/xxx/yyy
     * @param userId
     * @return
     */
    @GetMapping("get5/{userId}")
    public String getTest5(@PathVariable("userId") Integer userId){
        System.out.println(userId);
        return "成功";
    }

    /**
     * POST
     * Body raw -json格式请求参数
     * @param
     * @return
     */
    @PostMapping("post1")
    public String postTest1(@RequestBody UserDto user){
        System.out.println(user.getUserName());
        System.out.println(user.getPassword());
        return "成功";
    }

    @PostMapping("post2")    // 等同于上面的注解，是上面注解的简略化，表示，使用什么请求方式，以及请求路径，括号没有内容就没有路径，取父级路径（class上的）
    public String postTest2(HttpServletRequest request
            , @RequestParam("caseFile") MultipartFile caseFile
            , UserDto user) throws IOException, URISyntaxException {
        InputStream inputStream=caseFile.getInputStream();
        String caseData = IOUtils.toString(inputStream);
        System.out.println("caseData=="+caseData);

        System.out.println(user.getUserName());
        System.out.println(user.getPassword());


        return  "成功";
    }
}
