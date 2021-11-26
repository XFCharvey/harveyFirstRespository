package com.cbox.business.question.questiontitleoption.controller;

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
import com.cbox.business.question.questiontitleoption.service.QuestionTitleOptionService;


/**
 * @ClassName: QuestionTitleOptionController
 * @Function: 题目选项
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/questiontitleoption")
public class QuestionTitleOptionController extends BaseController {

	@Autowired
	private QuestionTitleOptionService questionTitleOptionService;

	
	/** listQuestionTitleOption : 获取题目选项列表数据 **/
	@RequestMapping(value = "listQuestionTitleOption", method = RequestMethod.POST)
	public TableDataInfo listQuestionTitleOption(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= questionTitleOptionService.listQuestionTitleOption(param);

		return getDataTable(list);
	}


	/** addQuestionTitleOption : 新增题目选项 **/
	@RequestMapping(value = "addQuestionTitleOption", method = RequestMethod.POST)
	public ResponseBodyVO addQuestionTitleOption(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("title_id,option_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionTitleOptionService.addQuestionTitleOption(param);
	}

	/** updateQuestionTitleOption : 修改题目选项 **/
	@RequestMapping(value = "updateQuestionTitleOption", method = RequestMethod.POST)
	public ResponseBodyVO updateQuestionTitleOption(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionTitleOptionService.updateQuestionTitleOption(param);
	}

	/** delQuestionTitleOption : 删除题目选项 **/
	@RequestMapping(value = "delQuestionTitleOption", method = RequestMethod.POST)
	public ResponseBodyVO delQuestionTitleOption(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionTitleOptionService.delQuestionTitleOption(param);
	}


}
