package com.hogwartsmini.demo.service;

import com.hogwartsmini.demo.common.PageTableResponse;
import com.hogwartsmini.demo.dto.RequestInfoDto;
import com.hogwartsmini.demo.dto.ResultDto;
import com.hogwartsmini.demo.dto.TokenDto;
import com.hogwartsmini.demo.dto.task.TestTaskDto;
import com.hogwartsmini.demo.entity.HogwartsTestCase;
import com.hogwartsmini.demo.entity.HogwartsTestTask;
import io.swagger.models.auth.In;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AiTestTaskService {

    /**
     * 新增测试任务信息
     * @param taskListId
     * @return
     */
    public ResultDto save(TestTaskDto taskDto, Integer taskListId) throws IOException, URISyntaxException;


    /**
     *  删除测试任务信息
     * @param taskId
     * @param createUserId
     * @return
     */
    ResultDto<HogwartsTestTask> delete(Integer taskId,Integer createUserId);

    /**
     *  修改测试任务信息
     * @param hogwartsTestTask
     * @return
     */
    ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask);

    /**
     *  根据id查询
     * @param taskId
     * @return
     */
    ResultDto<HogwartsTestTask> getById(Integer taskId,Integer createUserId);

    /**
     *  查询测试任务信息列表
     * @param hogwartsTestTask
     * @return
     */
    ResultDto<PageTableResponse<HogwartsTestTask>> list(HogwartsTestTask hogwartsTestTask);

    /**
     *  开始执行测试任务信息
     * @param hogwartsTestTask
     * @return
     */
    ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, HogwartsTestTask hogwartsTestTask) throws IOException, URISyntaxException;

    /**
     *  修改测试任务状态信息
     * @param hogwartsTestTask
     * @return
     */
    ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask);
}
