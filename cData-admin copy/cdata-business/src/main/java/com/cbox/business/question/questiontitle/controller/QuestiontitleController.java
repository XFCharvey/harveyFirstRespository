package com.cbox.business.question.questiontitle.controller;

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
import com.cbox.business.question.questiontitle.service.QuestiontitleService;


/**
 * @ClassName: QuestiontitleController
 * @Function: 题库题目
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/questiontitle")
public class QuestiontitleController extends BaseController {

	@Autowired
	private QuestiontitleService questiontitleService;

	
	/** listQuestiontitle : 获取题库题目列表数据 **/
	@RequestMapping(value = "listQuestiontitle", method = RequestMethod.POST)
	public TableDataInfo listQuestiontitle(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= questiontitleService.listQuestiontitle(param);

		return getDataTable(list);
	}

	/** getQuestiontitle : 获取指定id的题库题目数据 **/
	@RequestMapping(value = "getQuestiontitle", method = RequestMethod.POST)
	public ResponseBodyVO getQuestiontitle(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitleService.getQuestiontitle(param);
	}

	/** addQuestiontitle : 新增题库题目 **/
	@RequestMapping(value = "addQuestiontitle", method = RequestMethod.POST)
	public ResponseBodyVO addQuestiontitle(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("question_id,title_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitleService.addQuestiontitle(param);
	}

	/** updateQuestiontitle : 修改题库题目 **/
	@RequestMapping(value = "updateQuestiontitle", method = RequestMethod.POST)
	public ResponseBodyVO updateQuestiontitle(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitleService.updateQuestiontitle(param);
	}

	/** delQuestiontitle : 删除题库题目 **/
	@RequestMapping(value = "delQuestiontitle", method = RequestMethod.POST)
	public ResponseBodyVO delQuestiontitle(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questiontitleService.delQuestiontitle(param);
	}


}
