package com.hogwartsmini.demo.service.Impl;

import com.hogwartsmini.demo.common.PageTableRequest;
import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.common.TokenDb;
import com.hogwartsmini.demo.dao.HogwartsTestJenkinsMapper;
import com.hogwartsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.jenkins.AddHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartsmini.demo.dto.jenkins.UpdateHogwartsTestJenkinsDto;
import com.hogwartsmini.demo.entity.HogwartsTestJenkins;
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
public class AiTestJenkinsServiceImpl implements AiTestJenkinsService {

    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;
    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private TokenDb tokenDb;

    /**
     * 添加jenkins
     * @param hogwartsTestJenkins
     * @return
     */
    @Override
    public ResultDto<HogwartsTestJenkins> save(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins) throws IOException, URISyntaxException {
        //设置创建时间和更新时间
        hogwartsTestJenkins.setCreateTime(new Date());
        hogwartsTestJenkins.setUpdateTime(new Date());
//        JenkinsUtil.save(hogwartsTestJenkins);
        hogwartsTestJenkinsMapper.insertUseGeneratedKeys(hogwartsTestJenkins);
        //如果是否为默i认Jenkins的标志位为1，则修改hogwarts_test_ user 中的default_ jenkins_ id字段

        Integer jenkinsId=hogwartsTestJenkins.getId();
        if(Objects.nonNull(hogwartsTestJenkins.getDefaultJenkinsFlag()) && hogwartsTestJenkins.getDefaultJenkinsFlag()==1){
            log.info("查询用户信息jenkinsId："+jenkinsId+"用户id"+hogwartsTestJenkins.getCreateUserId());
            //将新增的JenkinsId放入其中，并根据用户id更新hogwarts_ test_ user,
            //更新token信息中的默认JenkinsId
            tokenDto.setDefaultJenkinsId(hogwartsTestJenkins.getId());
            tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
            hogwartsTestUserMapper.updateUserDefaultJenkinsId(jenkinsId,hogwartsTestJenkins.getCreateUserId());
        }

//        String defaultenkinsId=hogwartsTestJenkins.get
        return ResultDto.success("成功",hogwartsTestJenkins);
    }

    @Override
    public ResultDto<HogwartsTestJenkins> update(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins) throws IOException, URISyntaxException {
        //设置创建时间和更新时间
        hogwartsTestJenkins.setUpdateTime(new Date());
//        JenkinsUtil.save(hogwartsTestJenkins);
        //todo 到这里要继续搞
        hogwartsTestJenkinsMapper.updateByPrimaryKeySelective(hogwartsTestJenkins);
        //如果是否为默i认Jenkins的标志位为1，则修改hogwarts_test_ user 中的default_ jenkins_ id字段

        Integer jenkinsId=hogwartsTestJenkins.getId();
        if(Objects.nonNull(hogwartsTestJenkins.getDefaultJenkinsFlag()) && hogwartsTestJenkins.getDefaultJenkinsFlag()==1){
            log.info("查询用户信息jenkinsId："+jenkinsId+"用户id"+hogwartsTestJenkins.getCreateUserId());
            //将新增的JenkinsId放入其中，并根据用户id更新hogwarts_ test_ user,
            //更新token信息中的默认JenkinsId
            tokenDto.setDefaultJenkinsId(hogwartsTestJenkins.getId());
            tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
            hogwartsTestUserMapper.updateUserDefaultJenkinsId(jenkinsId,hogwartsTestJenkins.getCreateUserId());
        }

//        String defaultenkinsId=hogwartsTestJenkins.get
        return ResultDto.success("成功",hogwartsTestJenkins);
    }

    //删除jenkins
    @Override
    public ResultDto<HogwartsTestJenkins> delete(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins) throws IOException, URISyntaxException {
        log.info("要删除的jenkinsid为"+hogwartsTestJenkins.getId());
        hogwartsTestJenkinsMapper.deleteByPrimaryKey(hogwartsTestJenkins);
        return ResultDto.success("成功",hogwartsTestJenkins);
    }


    /**
     * 分页查询Jenkins列表
     * @param pageTableRequest
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestJenkins>> list( PageTableRequest<QueryHogwartsTestJenkinsListDto> pageTableRequest)  {
        Integer pageNum=pageTableRequest.getPageNum();
        Integer pageSize=pageTableRequest.getPageSize();
        QueryHogwartsTestJenkinsListDto params=pageTableRequest.getParams();
        log.info("Jenkins分页准备查询数据库，查询params"+params);
        log.info("Jenkins分页准备查询数据库，PageNum"+pageNum);
        log.info("Jenkins分页准备查询数据库，pageSize"+pageSize);
        List<HogwartsTestJenkins> list=hogwartsTestJenkinsMapper.list(params,(pageNum-1) * pageSize,pageSize);
        Integer count=hogwartsTestJenkinsMapper.count(params);

        PageTableResponse pageTableResponse = new PageTableResponse();
        pageTableResponse.setData(list);
        pageTableResponse.setRecordsTotal(count);
        return ResultDto.success("成功",pageTableResponse);
    }


}
