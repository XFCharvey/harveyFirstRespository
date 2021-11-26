package com.cbox.business.question.questionbank.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.question.questionbank.service.QuestionBankService;


/**
 * @ClassName: QuestionBankController
 * @Function: 题库主题
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/questionbank")
public class QuestionBankController extends BaseController {

	@Autowired
	private QuestionBankService questionBankService;

    /** listQuestionBank : 获取题库主题列表数据 **/
    @RequestMapping(value = "listQuestionBank", method = RequestMethod.POST)
    public TableDataInfo listQuestionBank(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = questionBankService.listQuestionBank(param, true);

        return getDataTable(list);
    }

    /** listQuestionBankWithCount : 获取问卷年数据，携带数量 **/
    @RequestMapping(value = "listQuestionBankWithCount", method = RequestMethod.POST)
    public TableDataInfo listQuestionBankWithCount(@RequestBody Map<String, Object> param) {

        List<Map<String, Object>> list = questionBankService.listQuestionBankWithCount(param);

        return getDataTable(list);
    }

    /** listQuestionBank : 获取题库主题列表数据 **/
    @RequestMapping(value = "listFilterInfoQuestionBank", method = RequestMethod.POST)
    public TableDataInfo listFilterInfoQuestionBank(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = questionBankService.listFilterInfoQuestionBank(param);

        return getDataTable(list);
    }
    
    /** getQuestionInfoById : 通过问卷id获取所有相关问卷的资料 **/
    @RequestMapping(value = "getQuestionInfoById", method = RequestMethod.POST)
    public String getQuestionInfoById(@RequestBody Map<String, Object> param) {

       String questionInfo = questionBankService.getQuestionInfoById(param);

        return questionInfo;
    }
    
    /** listQuestionBankApp : 获取题库主题列表数据 **/
    @RequestMapping(value = "listQuestionBankApp", method = RequestMethod.POST)
    public TableDataInfo listQuestionBankApp(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = questionBankService.listQuestionBank(param, false);

        return getDataTable(list);
    }

    /** listQuestionBank : 获取过滤后的题库主题列表数据 **/
    @RequestMapping(value = "filterQuestionBank", method = RequestMethod.POST)
    public TableDataInfo filterQuestionBank(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = questionBankService.filterQuestionBank(param);

        return getDataTable(list);
    }

	/** getQuestionBank : 获取指定id的题库主题数据 **/
	@RequestMapping(value = "getQuestionBank", method = RequestMethod.POST)
	public ResponseBodyVO getQuestionBank(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionBankService.getQuestionBank(param);
	}

	/** addQuestionBank : 新增题库主题 **/
	@RequestMapping(value = "addQuestionBank", method = RequestMethod.POST)
	public ResponseBodyVO addQuestionBank(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("q_type,q_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionBankService.addQuestionBank(param);
	}

    /** addQuestionBank : 新增题库主题 **/
    @RequestMapping(value = "copyQuestionBank", method = RequestMethod.POST)
    public ResponseBodyVO copyQuestionBank(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return questionBankService.copyQuestionBank(param);
    }

	/** updateQuestionBank : 修改题库主题 **/
	@RequestMapping(value = "updateQuestionBank", method = RequestMethod.POST)
	public ResponseBodyVO updateQuestionBank(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionBankService.updateQuestionBank(param);
	}

	/** delQuestionBank : 删除题库主题 **/
	@RequestMapping(value = "delQuestionBank", method = RequestMethod.POST)
	public ResponseBodyVO delQuestionBank(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionBankService.delQuestionBank(param);
	}



}
