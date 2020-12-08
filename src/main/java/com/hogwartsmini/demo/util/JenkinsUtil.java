package com.hogwartsmini.demo.util;

import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.io.ClassPathResource;



import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class JenkinsUtil {
    public static void save(HogwartsTestJenkins hogwartsTestJenkins) throws URISyntaxException, IOException {
//        String baseUrl="http://172.16.207.19:8001/jenkins";
//        String userName="admin";
//        String password="123456";
//        String jobName="test5";

        //读取创建job xml文件
        ClassPathResource classPathResource =new ClassPathResource("JenkinsConfigDir/hogwarts_test_jenkins_show.xml");
        InputStream inputStream=classPathResource.getInputStream();
        String jobConfiXml=FileUtil.getText(inputStream);

        //获取jenkins版本号
        JenkinsHttpClient jenkinsHttpClient=new JenkinsHttpClient(new URI(hogwartsTestJenkins.getUrl()),hogwartsTestJenkins.getUserName(),hogwartsTestJenkins.getPassword());
        String jenkinsVersion=jenkinsHttpClient.getJenkinsVersion();
        System.out.println(jenkinsVersion);
        //获取jenkin 里的jobs，并创建job
        JenkinsServer jenkinsServer=new JenkinsServer(jenkinsHttpClient);

       jenkinsServer.createJob(hogwartsTestJenkins.getName(),jobConfiXml,true);
//       jenkinsServer.updateJob(hogwartsTestJenkins.getName(),jobConfiXml,true);
        Map<String, Job> jobMap=jenkinsServer.getJobs();

        Job job=jobMap.get(hogwartsTestJenkins.getName());

        Map<String, String> map=new HashMap<>();
//        map.put("userId",hogwartsTestJenkins.getCreateUserId().toString());
//        map.put("remark",hogwartsTestJenkins.getRemark());
//        map.put()
        map=beanToMap(hogwartsTestJenkins);
        for(String key:map.keySet()){
            System.out.println(key);
        }
        job.build(map,true);
    }

    /**
     * 将对象属性转化为map结合
     */
    public static <T> Map<String, String> beanToMap(T bean) {
        Map<String, String> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key).toString());
            }
        }
        return map;
    }
}
