package com.hogwartsmini.demo.util;


import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

public class StrUtils {
    public static String list2String(List<Integer> caseIdList){
        if(Objects.isNull(caseIdList)){
            return null;
        }
        return caseIdList.toString()
                .replace("[","")
                .replace("]","");

    }

    /**
     * 提取请求的baseUrl,比如http://localhost:8081/hogwartsTask/
     * @param requestUrl
     * @return
     */
    public static String getHostAndPort(String requestUrl){
        if(StringUtils.isEmpty(requestUrl)){
            return "";
        }
        String http="";
        String tempUrl="";
        if (requestUrl.contains("://")){
            http=requestUrl.substring(0,requestUrl.indexOf("://")+3);
            tempUrl=requestUrl.substring(requestUrl.indexOf("://")+3);
        }
        if(tempUrl.contains("/")){
            tempUrl=tempUrl.substring(0,tempUrl.indexOf("/"));
        }
        return http+tempUrl;
    }
}
