package com.hogwartsmini.demo.service;

import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AiTestJenkinsService {

    /**
     * 新增jenkins
     * @param hogwartsTestJenkins
     * @return
     */
    public ResultDto<HogwartsTestJenkins> save(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins, AddHogwartsTestJenkinsDto addHogwartsTestJenkinsDto) throws IOException, URISyntaxException;
    /**
     * 分页查询Jenkins列表
     * @param pageTableRequest
     * @return
     */


    public ResultDto<PageTableResponse<HogwartsTestJenkins>> list(PageTableRequest<QueryHogwartsTestJenkinsListDto> pageTableRequest);
}
