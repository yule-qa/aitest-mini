package com.hogwartsmini.demo.dto;

import com.hogwartsmini.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "用户修改类",description = "请求类") // swagger文档 点model的提示信息
@Data
public class UpdateHogwartsTestUserDto extends BaseEntityNew {
        /**
         * 用户id
         */
        @ApiModelProperty(value="用户名",example = "ai-test-userId",required = true)  //swagger文档点model，这个添加的是example 里的值
        private Integer id;

        /**
         * 用户名
         */
        @ApiModelProperty(value="用户名",example = "ai-test-username",required = true)  //swagger文档点model，这个添加的是example 里的值
        private String userName;

        /**
         * 密码
         */
        @ApiModelProperty(value="密码",example = "ai-test-password",required = true)  //swagger文档点model，这个添加的是example 里的值
        private String password;

        /**
         * 邮箱
         */
        @ApiModelProperty(value="邮箱",example = "ai-test-username",required = true)  //swagger文档点model，这个添加的是example 里的值
        private String email;

        /**
         * 自动生成用例job名称 不为空时表示已经创建job
         */
        @ApiModelProperty(value="自动生成用例job名称",example = "ai-test-username")  //swagger文档点model，这个添加的是example 里的值
        private String autoCreateCaseJobName;

        /**
         * 执行测试job名称 不为空时表示已经创建job
         */
        @ApiModelProperty(value="执行测试job名称",example = "ai-test-username")  //swagger文档点model，这个添加的是example 里的值
        private String startTestJobName;

        /**
         * 默认Jenkins服务器
         */
        @ApiModelProperty(value="默认Jenkins服务器",example = "ai-test-username")  //swagger文档点model，这个添加的是example 里的值
        private Integer defaultJenkinsId;

}
