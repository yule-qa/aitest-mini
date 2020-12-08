package com.hogwartsmini.demo.service.Impl;

import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.dao.HogwartsTestJenkinsMapper;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
import com.hogwartsmini.demo.service.AiTestJenkinsService;
import com.hogwartsmini.demo.util.JenkinsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;


@Service
public class AiTestJenkinsServiceImpl implements AiTestJenkinsService {

    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;
    @Autowired
    private TokenDb tokenDb;

    /**
     * 添加jenkins
     * @param hogwartsTestJenkins
     * @return
     */
    @Override
    public ResultDto<HogwartsTestJenkins> save(HogwartsTestJenkins hogwartsTestJenkins) throws IOException, URISyntaxException {
        //设置创建时间和更新时间
        hogwartsTestJenkins.setCreateTime(new Date());
        hogwartsTestJenkins.setUpdateTime(new Date());
        JenkinsUtil.save(hogwartsTestJenkins);
        hogwartsTestJenkinsMapper.insertUseGeneratedKeys(hogwartsTestJenkins);

        return ResultDto.success("成功",hogwartsTestJenkins);
    }


}
