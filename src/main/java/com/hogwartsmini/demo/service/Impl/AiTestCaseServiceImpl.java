package com.hogwartsmini.demo.service.Impl;

import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.dao.HogwartsTestCaseMapper;
import com.hogwartsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.hogwartsmini.demo.service.AiTestCaseService;
import com.hogwartsmini.demo.service.AiTestJenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class AiTestCaseServiceImpl implements AiTestCaseService {

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;
    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private TokenDb tokenDb;

    /**
     * 添加jenkins
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto saveFile(TokenDto tokenDto, HogwartsTestCase hogwartsTestCase) throws IOException, URISyntaxException {
        //todo
        return ResultDto.success("成功",hogwartsTestCase);
    }




}
