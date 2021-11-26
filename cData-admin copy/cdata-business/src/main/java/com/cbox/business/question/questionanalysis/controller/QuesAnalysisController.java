package com.cbox.business.question.questionanalysis.controller;

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

import com.cbox.business.question.questionanalysis.service.QuesAnalysisService;

/**
 * @ClassName: QuestionAnalysisController
 * @Function: 问卷分析要素
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/question/quesanalysis")
public class QuesAnalysisController extends BaseController {

	@Autowired
	private QuesAnalysisService questionAnalysisService;

	/** listQuestionAnalysis : 获取问卷分析要素列表数据 **/
	@RequestMapping(value = "listQuestionAnalysis", method = RequestMethod.POST)
	public TableDataInfo listQuestionAnalysis(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = questionAnalysisService.listQuestionAnalysis(param);

		return getDataTable(list);
	}

	/** getQuestionAnalysis : 获取指定id的问卷分析要素数据 **/
	@RequestMapping(value = "getQuestionAnalysis", method = RequestMethod.POST)
	public ResponseBodyVO getQuestionAnalysis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionAnalysisService.getQuestionAnalysis(param);
	}

	/** addQuestionAnalysis : 新增问卷分析要素 **/
	@RequestMapping(value = "addQuestionAnalysis", method = RequestMethod.POST)
	public ResponseBodyVO addQuestionAnalysis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("item_type,item_key,item_name,question_id,rela_title_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionAnalysisService.addQuestionAnalysis(param);
	}

	/** updateQuestionAnalysis : 修改问卷分析要素 **/
	@RequestMapping(value = "updateQuestionAnalysis", method = RequestMethod.POST)
	public ResponseBodyVO updateQuestionAnalysis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionAnalysisService.updateQuestionAnalysis(param);
	}

	/** delQuestionAnalysis : 删除问卷分析要素 **/
	@RequestMapping(value = "delQuestionAnalysis", method = RequestMethod.POST)
	public ResponseBodyVO delQuestionAnalysis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return questionAnalysisService.delQuestionAnalysis(param);
	}

}
