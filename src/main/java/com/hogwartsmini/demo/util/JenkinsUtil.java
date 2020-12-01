package com.hogwartsmini.demo.util;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class JenkinsUtil {
    public static void build(String jobName,String userId,String remark,String testCommand) throws URISyntaxException, IOException {
        String baseUrl="http://172.16.207.19:8001/jenkins";
        String userName="admin";
        String password="123456";
//        String jobName="test5";

        //读取创建job xml文件
        ClassPathResource classPathResource =new ClassPathResource("JenkinsConfigDir/hogwarts_test_jenkins_show.xml");
        InputStream inputStream=classPathResource.getInputStream();
        String jobConfiXml=FileUtil.getText(inputStream);

        //获取jenkins版本号
        JenkinsHttpClient jenkinsHttpClient=new JenkinsHttpClient(new URI(baseUrl),userName,password);
        String jenkinsVersion=jenkinsHttpClient.getJenkinsVersion();
        System.out.println(jenkinsVersion);
        //获取jenkin 里的jobs，并创建job
        JenkinsServer jenkinsServer=new JenkinsServer(jenkinsHttpClient);

//       jenkinsServer.createJob(jobName,jobConfiXml,true);
       jenkinsServer.updateJob(jobName,jobConfiXml,true);
        Map<String, Job> jobMap=jenkinsServer.getJobs();

        Job job=jobMap.get(jobName);
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        map.put("remark",remark);
        map.put("testCommand",testCommand);

        job.build(map,true);
    }
}
