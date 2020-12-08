package com.hogwartsmini.demo.service;

import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AiTestJenkinsService {

    /**
     * 新增jenkins
     * @param hogwartsTestJenkins
     * @return
     */
    public ResultDto<HogwartsTestJenkins> save(HogwartsTestJenkins hogwartsTestJenkins) throws IOException, URISyntaxException;

}
