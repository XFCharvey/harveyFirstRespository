package com.cbox.business.question.analysis.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.business.question.analysis.service.QuestionAnalysisService;

/**
 * @ClassName: ExamRecordController
 * @Function: 问卷交叉分析
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/question/analysis")
public class QuestionAnalysisController extends BaseController {

	@Autowired
    private QuestionAnalysisService questionAnalysisService;
	
	 /**exportAnalysisInfo:根据题目信息，导出相关选票的excel文件
	 * @throws IOException **/
	@RequestMapping(value="exportAnalysisInfo",method=RequestMethod.POST)
	public String exportAnalysisInfo(@RequestBody Map<String,Object> param) throws IOException {
		
		return questionAnalysisService.exportAnalysisInfo(param);
	}



    /** listExamRecord : 获取问卷结果数据 **/
    @RequestMapping(value = "getQuestionResult", method = RequestMethod.POST)
    public ResponseBodyVO getQuestionResult(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return questionAnalysisService.getQuestionResult(param);
	}

    /** listExamRecord : 获取单个人的问卷结果数据 **/
    @RequestMapping(value = "getQuestionPersonResult", method = RequestMethod.POST)
    public ResponseBodyVO getQuestionPersonResult(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id,customer_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        return questionAnalysisService.getQuestionPersonResult(param);
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

    // 计算问卷的属性选项
    @PostMapping("calcQuestionOption")
    public ResponseBodyVO calcQuestionOption(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return questionAnalysisService.calcQuestionOption(param);
    }

    // 获得分析的选项数据
    @PostMapping("getAnalysisOption")
    public ResponseBodyVO getAnalysisOption(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return questionAnalysisService.getAnalysisOption(param);
    }

    // 设置每道题应用的分析项
    @PostMapping("setTitleAnalysisOption")
    public ResponseBodyVO setTitleAnalysisOption(@RequestBody Map<String, Object> param) {
        // 校验必填参数
        String checkResult = this.validInput("question_id,question_obj", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return questionAnalysisService.setTitleAnalysisOption(param);
    }



}
