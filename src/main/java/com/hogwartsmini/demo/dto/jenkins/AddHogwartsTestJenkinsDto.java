package com.hogwartsmini.demo.dto.jenkins;

import com.hogwartsmini.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@ApiModel(value = "添加Jenkins对象",description = "请求类") // swagger文档 点model的提示信息
@Data
public class AddHogwartsTestJenkinsDto extends BaseEntityNew {

    /**
     * 名称
     */
    @ApiModelProperty(value="Jenkins名称",required=true)
    private String name;

    /**
     * 测试命令
     */
    @ApiModelProperty(value="测试命令前缀",required=true)
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    @ApiModelProperty(value="Jenkins的baseUrl",required=true)
    private String url;

    /**
     * 用户名
     */
    @ApiModelProperty(value="Jenkins用户名称",required=true)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="Jenkins用户密码",required=true)
    private String password;


    /**
     * 备注
     */
    @ApiModelProperty(value="Jenkins备注")
    private String remark;

    /**
     * 命令运行的测试用例类型  1 文本 2 文件
     */
    @ApiModelProperty(value="命令运行的测试用例类型  1 文本 2 文件",required=true)
    private Byte commandRunCaseType;

    /**
     * 设置为默认服务器
     */
    @ApiModelProperty(value="是否设置为默认服务器 1 是 0 否",required=true)
    private Integer defaultJenkinsFlag = 0;

    /**
     * 测试用例后缀名 如果case为文件时，此处必填
     */
    @ApiModelProperty(value="测试用例后缀名 如果case为文件时，此处必填",required=true)
    private String commandRunCaseSuffix;


}
