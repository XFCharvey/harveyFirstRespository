package com.cbox.business.survey.survey.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;


import com.cbox.business.survey.survey.service.SurveyService;


/**
 * @ClassName: SurveyController
 * @Function: 客户调研
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/survey")
public class SurveyController extends BaseController {

	@Autowired
	private SurveyService surveyService;

	
	/** listSurvey : 获取客户调研列表数据 **/
	@RequestMapping(value = "listSurvey", method = RequestMethod.POST)
	public TableDataInfo listSurvey(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= surveyService.listSurvey(param);

		return getDataTable(list);
	}

	/** getSurvey : 获取指定id的客户调研数据 **/
	@RequestMapping(value = "getSurvey", method = RequestMethod.POST)
	public ResponseBodyVO getSurvey(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyService.getSurvey(param);
	}

	/** addSurvey : 新增客户调研 **/
	@RequestMapping(value = "addSurvey", method = RequestMethod.POST)
	public ResponseBodyVO addSurvey(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("survey_name,survey_date,file_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyService.addSurvey(param);
	}
	
	/** addSurveyByFile : 导入文件新增客户调研 
	 * @throws Exception **/
	@RequestMapping(value = "addSurveyByFile", method = RequestMethod.POST)
	public AjaxResult addSurveyByFile(@RequestBody Map<String, Object> param) throws Exception {

		// 校验必填参数
		String checkResult = this.validInput("survey_name,survey_date,file_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return AjaxResult.error(checkResult);
		}

		return surveyService.addSurveyByFile(param);
	}

	/** updateSurvey : 修改客户调研 **/
	@RequestMapping(value = "updateSurvey", method = RequestMethod.POST)
	public ResponseBodyVO updateSurvey(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyService.updateSurvey(param);
	}

	/** delSurvey : 删除客户调研 **/
	@RequestMapping(value = "delSurvey", method = RequestMethod.POST)
	public ResponseBodyVO delSurvey(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyService.delSurvey(param);
	}


}
