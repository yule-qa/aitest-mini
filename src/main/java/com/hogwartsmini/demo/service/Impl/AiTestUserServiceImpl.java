package com.hogwartsmini.demo.service.Impl;

import com.hogwartsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.UserDto;
import com.hogwartsmini.demo.entity.HogwartsTestUser;
import com.hogwartsmini.demo.service.AiTestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


//@Component //加上这个注解，AiTestUserServiceImpl就注入到了spring 容器里了，此时就找到了这个类
@Service
public class AiTestUserServiceImpl implements AiTestUserService {

    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;

    /**
     * 登录接口实现
     * @param userDto
     * @return
     */
    @Override
    public String login(UserDto userDto){
        /**
         * todo
         * 这里应该判断数据库是否存在该用户，密码是否正确，再return
         */
        System.out.println("用户名是"+userDto.getUserName());
        System.out.println("密码是"+userDto.getPassword());

        return userDto.getUserName()+"---"+userDto.getPassword();
    }

    /**
     * 注册接口实现
     * @param hogwartsTestUser
     * @return
     */
    @Override
    public ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser) {
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());

        hogwartsTestUserMapper.insertUseGeneratedKeys(hogwartsTestUser);
        return ResultDto.success("成功",hogwartsTestUser);
    }

    /**
     * 更新接口实现
     * @param hogwartsTestUser
     * @return
     */
    @Override
    public ResultDto<HogwartsTestUser> update(HogwartsTestUser hogwartsTestUser) {

        /** tk.mybatis的动态sql的使用
         * 使用这种方式，需要自己再mapper里定义updateUserDemo，并且在mapper.xml里写对应的sql ，
         * 下面这个方法修改后，只修改了用户名、密码、email。
         * 而原始数据，
         * 1.没有传入jenkins 相关数据库字段信息， 更新后，即便是发送请求，包含这些字段信息，因为sql没有修改对应字段，数据库仍旧为空
         * 2.原始数据包含创建时间，更新时间，因为sql没有修改对应字段，则数据库保留原有数据
         */
        hogwartsTestUserMapper.updateUserDemo(hogwartsTestUser.getUserName(),hogwartsTestUser.getPassword(),hogwartsTestUser.getEmail(),hogwartsTestUser.getId());
        /** tk.mybatis的Java API的使用
         *使用mpper 集成父类里自带的方法
         * 1. updateByPrimaryKeySelective，只修改不为空的字段，为空字段保留原有
         * 2. updateByprimaryKey, 根据主键，修改所有表中字段，如果没有传入，就会报错，
         */
        //hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);
        hogwartsTestUser.setCreateTime(new Date()); //这里的创建时间，应该是查库获取的，老师上课为了方便快捷，就这样写了
        hogwartsTestUser.setUpdateTime(new Date());
        hogwartsTestUserMapper.updateByPrimaryKey(hogwartsTestUser);



        return ResultDto.success("成功",hogwartsTestUser);
    }

    /**
     * 用户查询接口实现
     * @param hogwartsTestUser
     * @return
     */
    @Override
    public ResultDto<List<HogwartsTestUser>> getByName(HogwartsTestUser hogwartsTestUser) {
        /** tk.mybatis的动态sql的使用
         * 使用这种方式，需要自己再mapper里定义updateUserDemo，并且在mapper.xml里写对应的sql ，
         */
        //List<HogwartsTestUser> hogwartsTestUserList =hogwartsTestUserMapper.getByName(hogwartsTestUser.getUserName(),hogwartsTestUser.getId());
        /** tk.mybatis的Java API的使用
         *使用mpper 集成父类里自带的方法
         * 1. select，精确查询，不支持模糊
         */;
        List<HogwartsTestUser> hogwartsTestUserList=hogwartsTestUserMapper.select(hogwartsTestUser);
        return ResultDto.success("成功",hogwartsTestUserList);


    }

    @Override
    public ResultDto delete(Integer userId) {
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        hogwartsTestUser.setId(userId);
        hogwartsTestUserMapper.delete(hogwartsTestUser);
        return ResultDto.success("删除成功");
    }


}
