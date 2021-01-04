package com.hogwartsmini.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dto.RequestInfoDto;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.OperateJenkinsJobDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.hogwartsmini.demo.entity.HogwartsTestTask;
import com.hogwartsmini.demo.entity.HogwartsTestUser;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.apache.commons.lang.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.io.ClassPathResource;
import sun.awt.SunToolkit;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class JenkinsUtil {
    public static ResultDto<HogwartsTestUser> build(OperateJenkinsJobDto operateJenkinsJobDto) throws URISyntaxException, IOException {
        //获取构建Jenkins的相关参数
        TokenDto tokenDto=operateJenkinsJobDto.getTokenDto();
        HogwartsTestJenkins hogwartsTestJenkins=operateJenkinsJobDto.getHogwartsTestJenkins();
        HogwartsTestUser hogwartsTestUser=operateJenkinsJobDto.getHogwartsTestUser();
        Map<String,String> params=operateJenkinsJobDto.getParams();

        //读取创建job xml文件
        ClassPathResource classPathResource =new ClassPathResource("JenkinsConfigDir/hogwarts_start_jenkins_show.xml");
        InputStream inputStream=classPathResource.getInputStream();
        String jobConfiXml=FileUtil.getText(inputStream);
        //替换创建job xml里的参数

        //todo 增加非空等校验
        String baseUrl=hogwartsTestJenkins.getUrl();
        String userName=hogwartsTestJenkins.getUserName();
        String password=hogwartsTestJenkins.getPassword();
        System.out.println("jenkins请求地址"+baseUrl+"\n"+
                            "用户名"+userName+"\n"+
                            "密码"+password);

        //如果当遇见，jenkins并没有提供用户名密码，而是提供授权的方式，可以通过token当密码，进行唤起jenkins client

        //获取jenkins客户端对象，并获取版本号
        JenkinsHttpClient jenkinsHttpClient=new JenkinsHttpClient(new URI(baseUrl),userName,password);
        String jenkinsVersion=jenkinsHttpClient.getJenkinsVersion();
        System.out.println("jenkins的版本号"+jenkinsVersion);
        //获取jenkin 里的jobs，并创建job
        JenkinsServer jenkinsServer=new JenkinsServer(jenkinsHttpClient);
        String jobName = "aitest_start_job"+hogwartsTestUser.getId();
        //执行测试job名称， 不为空时表示已经创建job
        if(StringUtils.isEmpty(hogwartsTestUser.getStartTestJobName())){
            jenkinsServer.createJob(jobName,jobConfiXml,true);
            hogwartsTestUser.setStartTestJobName(jobName);
        }else{
            jenkinsServer.updateJob(jobName,jobConfiXml,true);
        }

        Map<String, Job> jobMap=jenkinsServer.getJobs();
        //根据job名称获取Jenkins上的job信息
        Job job=jobMap.get(jobName);
        //为构建参数赋值
        Map<String, String> map=params;
        //开始有参构建
        job.build(map,true);


        return ResultDto.success("成功",hogwartsTestUser);
   }



    public static StringBuilder getUpdateTaskStatusUrl(RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask){
        StringBuilder updateStatusUrl=new StringBuilder();
        updateStatusUrl.append("curl -X PUT ");
        updateStatusUrl.append("\""+requestInfoDto.getBaseUrl()+"/task/status \" ");
        updateStatusUrl.append("-H \"Content-Type: application/json\" ");
        updateStatusUrl.append("-H \"token:"+requestInfoDto.getToken()+"\" ");
        updateStatusUrl.append("-d ");

        JSONObject json=new JSONObject();
        json.put("taskId",hogwartsTestTask.getId());
        json.put("status", UserBaseStr.STATUS_THREE);
        json.put("buildUrl","${BUILD_URL}");

        updateStatusUrl.append("'"+json.toJSONString()+"'");

        return updateStatusUrl;

    }


}
