package com.hogwartsmini.demo.service.Impl;


import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.dao.*;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.entity.HogwartsTestTask;
import com.hogwartsmini.demo.service.AiTestTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;


@Service
@Slf4j
public class AiTestTaskServiceImpl implements AiTestTaskService {

    @Autowired
    private HogwartsTestTaskMapper hogwartsTestTaskMapper;
    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;
    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;
    @Autowired
    private HogwartsTestTaskCaseRelMapper hogwartsTestTaskCaseRelMapper;

    @Autowired
    private TokenDb tokenDb;

    /**
     * 添加jenkins
     * @param hogwartsTestTask
     * @return
     */
    @Override
    public ResultDto save(TokenDto tokenDto, HogwartsTestTask hogwartsTestTask) throws IOException, URISyntaxException {
        //todo
        return ResultDto.success("成功",hogwartsTestTask);
    }

    @Override
    public ResultDto<HogwartsTestTask> delete(Integer taskId, Integer createUserId) {
        return null;
    }

    @Override
    public ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask) {
        return null;
    }

    @Override
    public ResultDto<HogwartsTestTask> getById(Integer taskId, Integer createUserId) {
        return null;
    }

//    @Override
//    public ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest<QueryHogwartsTestTaskListDto> pageTableRequest) {
//        return null;
//    }
//
//    @Override
//    public ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask) throws IOException {
//        return null;
//    }

    @Override
    public ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask) {
        return null;
    }


}
