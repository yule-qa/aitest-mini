package com.hogwartsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.ServiceException;
import com.hogwartsmini.demo.dto.AddHogwartsTestUserDto;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.UpdateHogwartsTestUserDto;
import com.hogwartsmini.demo.dto.UserDto;
import com.hogwartsmini.demo.entity.HogwartsTestUser;
import com.hogwartsmini.demo.service.AiTestUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "霍格沃兹测试学院--用户管理")
@RestController
@RequestMapping("user")
@Slf4j  //java 里的一个日志规范,加上这个注释，就可以使用log.方法名（)了
public class AiTestUserController {

    @Autowired
    private AiTestUserService aiTestUserService;
    @Value("${AI.key1}")
    private String aikey1; //在配置文件application-dev.yaml里配置了信息

    @ApiOperation("登录接口")
    //@RequestMapping(value ="login",method = RequestMethod.POST)
    @PostMapping("login")    // 等同于上面的注解，是上面注解的简略化
    public String login(@RequestBody UserDto userDto){
        String result=aiTestUserService.login(userDto);

        if(userDto.getUserName().contains("error2")){
            throw new NullPointerException();
        }
        if(userDto.getUserName().contains("error")) {
            ServiceException.throwEx("用户名中含有error");
        }
        return "成功"+result+"        aikey1="+aikey1;
    }

    @ApiOperation("注册接口")
    @PostMapping("register")
    public ResultDto register(@RequestBody AddHogwartsTestUserDto addHogwartsTestUserDto){
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        BeanUtils.copyProperties(addHogwartsTestUserDto,hogwartsTestUser);
        // 判断传入参数
        if(StringUtils.isEmpty(addHogwartsTestUserDto.getUserName())){
            return ResultDto.fail("用户名不能为空");
        }
        if(StringUtils.isEmpty(addHogwartsTestUserDto.getPassword())){
            return ResultDto.fail("密码不能为空");
        }
        log.info("用户注册接口 请求入参"+JSONObject.toJSONString(hogwartsTestUser));

        return aiTestUserService.save(hogwartsTestUser);
    }

    @ApiOperation("用户信息 修改接口")
    @PutMapping("update")
    public ResultDto update(@RequestBody UpdateHogwartsTestUserDto updateHogwartsTestUserDto){
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        BeanUtils.copyProperties(updateHogwartsTestUserDto,hogwartsTestUser);
        // 判断传入参数
        if(StringUtils.isEmpty(updateHogwartsTestUserDto.getUserName())){
            return ResultDto.fail("用户名不能为空");
        }
        if(StringUtils.isEmpty(updateHogwartsTestUserDto.getPassword())){
            return ResultDto.fail("密码不能为空");
        }
        log.info("用户信息修改接口 请求入参"+JSONObject.toJSONString(hogwartsTestUser));

        return aiTestUserService.update(hogwartsTestUser);
    }


//    @RequestMapping(value="byId/{userId}/{id}",method=RequestMethod.GET)
//    public String getById(@PathVariable("userId") Long userId,@PathVariable("id") Long id){
//        System.out.println("用户id为"+userId);
//        System.out.println("id为"+id);
//        return "成功"+userId+" id"+id;
//    }

  //  @RequestMapping(value="byId",method=RequestMethod.GET)
//    @GetMapping("byId")
//    public String getById1(@RequestParam(value="userId",required = false) Long userId,@RequestParam("id") Long id){
//        System.out.println("RequestParam 用户id为"+userId);
//        System.out.println("RequestParam id为"+id);
//        return "RequestParam 成功"+userId+" id"+id;
//    }


    @ApiOperation("用户信息 查询接口")
    @GetMapping("byName")
    public ResultDto<List<HogwartsTestUser>> getByName(@RequestParam(value="userId",required = false) Integer userId, @RequestParam(value="userName",required = false) String userName){
        log.info("根据用户名模糊查询 入参"+userName);
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        hogwartsTestUser.setId(userId);
        hogwartsTestUser.setUserName(userName);

        return aiTestUserService.getByName(hogwartsTestUser);
    }



    @ApiOperation("用户信息 删除接口")
    @DeleteMapping("{userId}")  //这里使用了动态路径，路径参数是 @PathVariable("userId"），即：http://localhost:8081/user/5
    public ResultDto delete(@PathVariable("userId") Integer userId){
        log.info("根据用户id删除用户"+userId);
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        hogwartsTestUser.setId(userId);
        return aiTestUserService.delete(userId);
    }

}
