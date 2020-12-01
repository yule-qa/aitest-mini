package com.hogwartsmini.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户登录类",description = "请求类") // swagger文档 点model的提示信息
@Data
public class UserDto {

    @ApiModelProperty(value="用户名",example = "ai-test-username",required = true)  //swagger文档点model，这个添加的是example 里的值
    private String userName;

    @ApiModelProperty(value="密码",example = "ai-test-password",required = true)
    private String password;

}
