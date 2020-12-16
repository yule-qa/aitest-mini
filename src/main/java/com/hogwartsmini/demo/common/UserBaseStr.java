package com.hogwartsmini.demo.common;

/**
 * 这个定义用户的常量类
 */
public class UserBaseStr {
    //获取请求的header中token值
    public static final String LOGIN_TOKEN="token";
    //登录密码的加密盐值
    public static final String md5Hex_sign="HogwartsTest";
    //任务类型 默认1
    public static final Integer Task_Type_One=1;
    //任务状态 1新建
    public static final Integer STATUS_ONE=1;
    //任务状态 2执行中
    public static final Integer STATUS_TWO=2;
    //任务状态 3执行完成
    public static final Integer STATUS_THREE=3;
    //测试用例类型 1 文本类型
    public static final Integer CASE_TYPE_ONE=1;
    //测试用例类型 2 文件类型
    public static final Integer CASE_TYPE_TWO=2;
}
