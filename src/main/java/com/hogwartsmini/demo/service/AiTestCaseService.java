package com.hogwartsmini.demo.service;

import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AiTestCaseService {

    /**
     * 添加文件接口
     * @param hogwartsTestCase
     * @return
     */
    public ResultDto save(TokenDto tokenDto, HogwartsTestCase hogwartsTestCase) throws IOException, URISyntaxException;
    public ResultDto list(PageTableRequest<QueryHogwartsTestCaseListDto> pageTableRequest);
    public ResultDto delete(HogwartsTestCase hogwartsTestCase);

}
