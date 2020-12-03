package com.hogwartsmini.demo.service.Impl;

import com.hogwartsmini.demo.common.HogwartsToken;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.UserDto;
import com.hogwartsmini.demo.entity.HogwartsTestUser;
import com.hogwartsmini.demo.service.AiTestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;


//@Component //加上这个注解，AiTestUserServiceImpl就注入到了spring 容器里了，此时就找到了这个类
@Service
public class AiTestUserServiceImpl implements AiTestUserService {

    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private TokenDb tokenDb;

    /**
     * 登录接口实现
     * @param userDto
     * @return
     */
    @Override
    public ResultDto<HogwartsToken> login(UserDto userDto){
        /**
         * 登录接口实现
         * 这里应该判断数据库是否存在该用户，密码是否正确，再return
         */
        String userName=userDto.getUserName();
        String password=userDto.getPassword();
        System.out.println("用户名是"+userName);
        System.out.println("密码是"+userName);
        //1、 获取用户录入的用户名/密码并用MD5加密String newPwd = DigestUtils.md5Diges tAsHex()
        String newPwd=DigestUtils.md5DigestAsHex((UserBaseStr.md5Hex_sign+userName+password).getBytes());
        //2、 根据用户名+新密码查询数据库中是否存在数据
        HogwartsTestUser queryHogwartsTestUser=new HogwartsTestUser();
        queryHogwartsTestUser.setUserName(userName);
        queryHogwartsTestUser.setPassword(newPwd);

        HogwartsTestUser resultHogwartsTestUser=hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);

        //3、若不存在提示:用户不存在或密码错误
        if(Objects.isNull(resultHogwartsTestUser)){
            return ResultDto.fail("用户不存在或密码错误");
        }
        //4、若存在，则创建Token对象，生成token并将相关信息存入TokenDto
        String tokenStr=DigestUtils.md5DigestAsHex((System.currentTimeMillis()+userName+password).getBytes());
        HogwartsToken hogwartsToken=new HogwartsToken();
        hogwartsToken.setToken(tokenStr);
        TokenDto tokenDto=new TokenDto();
        tokenDto.setUserId(resultHogwartsTestUser.getId());
        tokenDto.setUserName(resultHogwartsTestUser.getUserName());
        tokenDto.setDefaultJenkinsId(resultHogwartsTestUser.getDefaultJenkinsId());
        tokenDto.setToken(tokenStr);
        tokenDto.setUserName(resultHogwartsTestUser.getUserName());
        tokenDto.setUserName(resultHogwartsTestUser.getUserName());

        //存入缓存文件TokenDb
        tokenDb.addUserInfo(tokenStr,tokenDto);

        return ResultDto.success("成功",hogwartsToken);
    }

    /**
     * 注册接口实现
     * @param hogwartsTestUser
     * @return
     */
    @Override
    public ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser) {
        //在调用数据库前，先判断数据库是否存在用户，然后将密码做md5加密
        //1、 校验用户名是否已经存在
        String userName= hogwartsTestUser.getUserName();

        HogwartsTestUser queryHogwartsTestUser=new HogwartsTestUser();
        queryHogwartsTestUser.setUserName(userName);
        HogwartsTestUser resultHogwartsTestUser=hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);
        if(Objects.nonNull(resultHogwartsTestUser)){
            return ResultDto.fail("用户名已存在！");
        }

        //2.  密码MD5加密存储:
        String password= hogwartsTestUser.getPassword();
        String newPwd= DigestUtils.md5DigestAsHex((UserBaseStr.md5Hex_sign+userName+password).getBytes());
        hogwartsTestUser.setPassword(newPwd);
        //3.创建用户
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
