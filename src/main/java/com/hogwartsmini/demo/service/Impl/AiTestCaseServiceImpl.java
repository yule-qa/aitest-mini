package com.hogwartsmini.demo.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.common.UserBaseStr;
import com.hogwartsmini.demo.dao.HogwartsTestCaseMapper;
import com.hogwartsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.dto.testcase.QueryHogwartsTestCaseListDto;
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
    private TokenDb tokenDb;

    /**
     * 添加jenkins
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> save(TokenDto tokenDto, HogwartsTestCase hogwartsTestCase) throws IOException, URISyntaxException {
        hogwartsTestCase.setCreateTime(new Date());
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCase.setDelFlag(UserBaseStr.DEL_FLAG_ONE);
        hogwartsTestCaseMapper.insertUseGeneratedKeys(hogwartsTestCase);
        return ResultDto.success("成功",hogwartsTestCase);
    }

    @Override
    public ResultDto list(PageTableRequest<QueryHogwartsTestCaseListDto> pageTableRequest) {
        Integer pageNum=pageTableRequest.getPageNum();
        Integer pageSize=pageTableRequest.getPageSize();
        QueryHogwartsTestCaseListDto params=pageTableRequest.getParams();
        log.info("测试用例分页准备查询数据库，查询params"+params);
        log.info("测试用例分页准备查询数据库，PageNum"+pageNum);
        log.info("测试用例分页准备查询数据库，pageSize"+pageSize);
        List<HogwartsTestCase> list=hogwartsTestCaseMapper.list(params,(pageNum-1) * pageSize,pageSize);
        log.info("测试用例查询数据库，返回的list为"+list);
        Integer count=hogwartsTestCaseMapper.count(params);

        PageTableResponse pageTableResponse = new PageTableResponse();
        pageTableResponse.setData(list);
        pageTableResponse.setRecordsTotal(count);
        log.info("测试用例列表查询，返回消息体"+ JSONObject.toJSONString(pageTableResponse));
        return ResultDto.success("成功",pageTableResponse);
    }

    @Override
    public ResultDto delete(HogwartsTestCase hogwartsTestCase) {
        log.info("准备删除测试用例"+hogwartsTestCase.getId());
        hogwartsTestCaseMapper.deleteByPrimaryKey(hogwartsTestCase);
        return ResultDto.success("删除成功",hogwartsTestCase);
    }


}
