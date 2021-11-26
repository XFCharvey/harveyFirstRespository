package com.cbox.business.timetask.mock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbox.base.utils.StringUtils;
import com.cbox.business.timetask.mock.service.ActivityTaskService;
import com.cbox.business.timetask.mock.service.MockCustomerService;
import com.cbox.business.timetask.mock.service.MockHousesService;
import com.cbox.business.timetask.mock.service.MockProjectService;
import com.cbox.business.timetask.mock.service.MockShowService;
import com.cbox.business.timetask.mock.service.MockTaskService;
import com.cbox.business.timetask.mock.service.MockYLProblemGdkfService;

/**
 * @ 定时更新活动状态
 *
 */

@Component("MockTask")
public class MockTaskController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockTaskService mockTaskService;

    @Autowired
    private MockHousesService mockHousesService;

    @Autowired
    private MockProjectService mockProjectService;

    @Autowired
    private MockCustomerService mockCustomerService;

    @Autowired
    private MockShowService mockShowService;

    @Autowired
    private MockYLProblemGdkfService mMockYLProblemGdkfService;

    @Autowired
    ActivityTaskService activityTaskService;

    public void activityStatusUp() {
        logger.info(StringUtils.format("检查是否有过期的活动并修改状态"));
        activityTaskService.activityStatusUp();
    }

    public void MockTaskData() {
        logger.info(StringUtils.format("定时获取融创系统任务工单数据"));
        mockTaskService.getTaskWork();
    }

    /** 初始执行一次*/
    public void getTaskWorkHis() {
        logger.info(StringUtils.format("初始化抓取所有任务工单数据"));
        mockTaskService.getTaskWorkHis();
    }

    public void MockHouses() {
        logger.info(StringUtils.format("定时获取融创系统房源数据"));
        mockHousesService.callHouses();
    }

    public void MockProject() {
        logger.info(StringUtils.format("定时获取融创系统项目数据"));
        mockProjectService.getProject();
    }

    public void MockCustomer() {
        logger.info(StringUtils.format("定时获取融创系统客户数据"));
        mockCustomerService.callCustomer();
    }

    public void MockShow() {
        logger.info(StringUtils.format("定时获取融创系统大屏展示数据"));
        mockShowService.getShow();
    }

    public void MockProblemAndHandover() {
        logger.info(StringUtils.format("定时获取云链工地开发和交付问题及数据"));
        mMockYLProblemGdkfService.callProblemGdkf();
    }

}
