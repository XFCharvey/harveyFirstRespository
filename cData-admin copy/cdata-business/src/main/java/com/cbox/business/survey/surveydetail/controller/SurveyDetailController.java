package com.cbox.business.survey.surveydetail.controller;

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


import com.cbox.business.survey.surveydetail.service.SurveyDetailService;


/**
 * @ClassName: SurveyDetailController
 * @Function: 客户调研
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/survey/surveydetail/surveydetail")
public class SurveyDetailController extends BaseController {

	@Autowired
	private SurveyDetailService surveyDetailService;

	
	/** listSurveyDetail : 获取客户调研列表数据 **/
	@RequestMapping(value = "listSurveyDetail", method = RequestMethod.POST)
	public TableDataInfo listSurveyDetail(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= surveyDetailService.listSurveyDetail(param);

		return getDataTable(list);
	}

	/** getSurveyDetail : 获取指定id的客户调研数据 **/
	@RequestMapping(value = "getSurveyDetail", method = RequestMethod.POST)
	public ResponseBodyVO getSurveyDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyDetailService.getSurveyDetail(param);
	}

	/** addSurveyDetail : 新增客户调研 **/
	@RequestMapping(value = "addSurveyDetail", method = RequestMethod.POST)
	public ResponseBodyVO addSurveyDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("survey_id,send_time,survey_time,project_id,adviser_name,adviser_name_up,engineer_name,node_name,house_type,area,total_price,customer_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyDetailService.addSurveyDetail(param);
	}

	/** updateSurveyDetail : 修改客户调研 **/
	@RequestMapping(value = "updateSurveyDetail", method = RequestMethod.POST)
	public ResponseBodyVO updateSurveyDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyDetailService.updateSurveyDetail(param);
	}

	/** delSurveyDetail : 删除客户调研 **/
	@RequestMapping(value = "delSurveyDetail", method = RequestMethod.POST)
	public ResponseBodyVO delSurveyDetail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return surveyDetailService.delSurveyDetail(param);
	}


}
