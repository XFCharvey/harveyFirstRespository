package com.cbox.business.question.questiontitlegroup.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;


import com.cbox.business.question.questiontitlegroup.service.QuestiontitlegroupService;


/**
 * @ClassName: QuestiontitlegroupController
 * @Function: 题目分组
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/question/questiontitlegroup")
public class QuestiontitlegroupController extends BaseController {

	@Autowired
	private QuestiontitlegroupService questiontitlegroupService;

	
	/** listQuestiontitlegroup : 获取题目分组列表数据 **/
	@RequestMapping(value = "listQuestiontitlegroup", method = RequestMethod.POST)
	public TableDataInfo listQuestiontitlegroup(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= questiontitlegroupService.listQuestiontitlegroup(param);

		return getDataTable(list);
	}

	/** getQuestiontitlegroup : 获取指定id的题目分组数据 **/
	@RequestMapping(value = "getQuestiontitlegroup", method = RequestMethod.POST)
	public ResponseBodyVO getQuestiontitlegroup(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitlegroupService.getQuestiontitlegroup(param);
	}

	/** addQuestiontitlegroup : 新增题目分组 **/
	@RequestMapping(value = "addQuestiontitlegroup", method = RequestMethod.POST)
	public ResponseBodyVO addQuestiontitlegroup(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("question_id,group_name,group_sort", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitlegroupService.addQuestiontitlegroup(param);
	}

	/** updateQuestiontitlegroup : 修改题目分组 **/
	@RequestMapping(value = "updateQuestiontitlegroup", method = RequestMethod.POST)
	public ResponseBodyVO updateQuestiontitlegroup(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitlegroupService.updateQuestiontitlegroup(param);
	}

	/** delQuestiontitlegroup : 删除题目分组 **/
	@RequestMapping(value = "delQuestiontitlegroup", method = RequestMethod.POST)
	public ResponseBodyVO delQuestiontitlegroup(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitlegroupService.delQuestiontitlegroup(param);
	}


}
