package com.cbox.reward.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.customer.customer.service.CustomerService;
import com.cbox.business.question.analysis.service.QuestionAnalysisService;
import com.cbox.business.question.questionbank.service.QuestionBankService;
import com.cbox.reward.service.DictDataService;
import com.cbox.reward.service.RewardService;

/**
 * 问卷调查
 *
 */
@RestController
@RequestMapping("/lottery")
public class QuestionnaireController extends BaseController {

    @Autowired
    RewardService lotteryService;

    @Autowired
    QuestionAnalysisService questionAnalysisService;

    @Autowired
    QuestionBankService questionBankService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DictDataService dictDataService;

    @PostMapping("saveExam")
    public ResponseBodyVO saveExam(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("qbank_id,name,phone,deviceid,work,birthyear,sex", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        return lotteryService.saveExam(param);
    }

    // 获取问卷的题目
    @PostMapping("getQuestion")
    public ResponseBodyVO getQuestion(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        param.put("has_result", "0");

        return questionAnalysisService.getQuestionResult(param);
    }

    // 获取问卷信息
    @PostMapping("getQuestionBank")
    public ResponseBodyVO getQuestionBank(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        param.put("rec_id", StrUtil.getMapValue(param, "question_id"));

        return questionBankService.getQuestionBank(param);
    }

    // 得到客户信息
    @PostMapping("getCustomer")
    public ResponseBodyVO getCustomer(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("cust_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        param.put("rec_id", StrUtil.getMapValue(param, "cust_id"));

        return dictDataService.getCustomer(param);
    }

    // 得到职业的字典数据
    @PostMapping("getDictData")
    public ResponseBodyVO getDictData(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("dict_type", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return dictDataService.getDictData(param);
    }

}