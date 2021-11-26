package com.cbox.business.timetask.rule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbox.base.utils.StringUtils;
import com.cbox.business.timetask.rule.service.RuleActivityService;
import com.cbox.business.timetask.rule.service.RuleComplainService;
import com.cbox.business.timetask.rule.service.RuleHouseService;
import com.cbox.business.timetask.rule.service.RuleProjectStatusService;
import com.cbox.business.timetask.rule.service.RuleSignHouseService;

/**
 * @ 定时的业务规则
 *
 */

@Component("TimeRule")
public class TimeRuleController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RuleActivityService ruleActivityService;

    @Autowired
    private RuleHouseService ruleHouseService;

    @Autowired
    private RuleSignHouseService ruleSignHouseService;

    @Autowired
    private RuleComplainService ruleComplainService;

    @Autowired
    private RuleProjectStatusService ruleProjectStatusService;


    public void callActivityRule() {
        logger.info(StringUtils.format("定时触发活动后调研"));
        ruleActivityService.callQuestion();
    }

    public void callHouseRule() {
        logger.info(StringUtils.format("定时触发交房后调研"));
        ruleHouseService.callQuestion();
    }

    public void callSignHouseRule() {
        logger.info(StringUtils.format("定时触发签约后调研"));
        ruleSignHouseService.callSignRule();
    }

    public void callComplainSendRule() {
        logger.info(StringUtils.format("定时触发投诉单派发跟踪任务单"));
        ruleComplainService.callSendTask();
    }

    public void callProjectStatusRule() {
        logger.info(StringUtils.format("定时刷新项目及节点状态"));
        ruleProjectStatusService.refreshStatus();
    }


}
