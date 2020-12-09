package com.hogwartsmini.demo.common;

import com.hogwartsmini.demo.dto.TokenDto;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 这个文件用于缓存所有token信息
 */

@Component  // 这个单词的意思是组件，就是声明这个类为spring容器里的一个组件，即是将这个类注册到spring容器里，别处可以用@Autowired引入使用
public class TokenDb {
    //1、定义用于缓存token的map(key=token)
    private Map<String, TokenDto> tokenMap=new HashMap();
    //2、获取在线用户数(可选功能)
    public Integer getOnlineUserSize(){
        return tokenMap.size();
    }
    //3、根据token获取TokenDto(userId/userName/defaultJenkinsId/token)
    public TokenDto getUserInfo(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return tokenMap.get(token);
    }
    //4、用户登录时新增token和TokenDto
    public void addUserInfo(String token,TokenDto tokenDto){
        if (tokenDto == null){
            return;
        }
        tokenMap.put(token,tokenDto);
    }
    //5、用户退出时根据token移除TokenDto
    public TokenDto removeUserInfo(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return tokenMap.remove(token);

    }
    //6、判断token是否有效,true 是，false 否
    public boolean isLogin(String token){
        return tokenMap.get(token) !=null;
    }

    //也可以实现成登录用户互踢，2种方式，1是id前后缀，2是id-token=map的key-value
    public TokenDto addTokenDto(String token,TokenDto tokenDto){

        if(Objects.isNull(tokenDto)){
            return tokenDto;
        }

        return tokenMap.put(token,tokenDto);
    }
}
