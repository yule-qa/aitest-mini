package com.hogwartsmini.demo.service;

import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.UserDto;
import com.hogwartsmini.demo.entity.HogwartsTestUser;

import java.util.List;

public interface AiTestUserService{

    /**
     * 登录
     * @param userDto
     * @return
     */
    public String login(UserDto userDto);

    /**
     * 注册用户
     * @param hogwartsTestUser
     * @return
     */
    public ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser);

    /**
     * 更新用户
     */
    public ResultDto<HogwartsTestUser> update(HogwartsTestUser hogwartsTestUser);

    /**
     * 根据用户id或名称模糊查询
     */
    public ResultDto<List<HogwartsTestUser>> getByName(HogwartsTestUser hogwartsTestUser);

    /**
     * 根据用户id删除用户
     */
    public ResultDto<HogwartsTestUser> delete (Integer userId);

}
