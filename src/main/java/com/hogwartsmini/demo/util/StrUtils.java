package com.hogwartsmini.demo.util;

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
}
