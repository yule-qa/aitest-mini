package com.hogwartsmini.demo.dao;

import com.hogwartsmini.demo.common.MySqlExtensionMapper;
import com.hogwartsmini.demo.entity.HogwartsTestUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository    //dao层加了这个注解，就生成了一个bean，将这个bean 注入容器，别处就可以引用了
public interface HogwartsTestUserMapper extends MySqlExtensionMapper<HogwartsTestUser> {

    //这里@Param 括号里的值，代表mapper.xml配置文件里的值，必须一一对应
    int updateUserDemo(@Param("username") String username,@Param("password") String password,@Param("email") String email,@Param("id") Integer id);
    List<HogwartsTestUser> getByName(@Param("userName") String userName, @Param("userId") Integer userId);
    int updateUserDefaultJenkinsId(@Param("defaultJenkinsId") Integer defaultJenkinsId, @Param("userId") Integer userId);

}