package com.cbox.business.survey.surveydetail.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.annotation.DataScope;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.SecurityUtils;

import com.cbox.business.survey.surveydetail.mapper.SurveyDetailMapper;

/**
 * @ClassName: SurveyDetailService
 * @Function: 客户调研
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class SurveyDetailService extends BaseService {

	@Autowired
	private SurveyDetailMapper surveyDetailMapper;

	/** listSurveyDetail : 获取客户调研列表数据 **/
	public List<Map<String, Object>> listSurveyDetail(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = surveyDetailMapper.listSurveyDetail(param);

		return list;
	}

	/** getSurveyDetail : 获取指定id的客户调研数据 **/
	public ResponseBodyVO getSurveyDetail(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = surveyDetailMapper.getSurveyDetail(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addSurveyDetail : 新增客户调研 **/
	public ResponseBodyVO addSurveyDetail(Map<String, Object> param) {

		// Table:d_survey_Detail
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("survey_id", param.get("survey_id"));
		mapParam.put("send_time", param.get("send_time"));
		mapParam.put("survey_time", param.get("survey_time"));
		mapParam.put("project_name", param.get("project_name"));
		mapParam.put("counselor", param.get("counselor"));
		mapParam.put("counselor_f", param.get("counselor_f"));
		mapParam.put("engineer_name", param.get("engineer_name"));
		mapParam.put("node_name", param.get("node_name"));
		mapParam.put("house_type", param.get("house_type"));
		mapParam.put("house_area", param.get("house_area"));
		mapParam.put("total_price", param.get("total_price"));
		mapParam.put("house_name", param.get("house_name"));
		mapParam.put("customer_name", param.get("customer_name"));
		int count = this.save("d_survey_Detail", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增客户调研失败");
	}

	/** updateSurveyDetail : 修改客户调研 **/
	public ResponseBodyVO updateSurveyDetail(Map<String, Object> param) {

		// Table:d_survey_Detail
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("survey_id", param.get("survey_id"));
		mapParam.put("send_time", param.get("send_time"));
		mapParam.put("survey_time", param.get("survey_time"));
		mapParam.put("project_name", param.get("project_name"));
		mapParam.put("counselor", param.get("counselor"));
		mapParam.put("counselor_f", param.get("counselor_f"));
		mapParam.put("engineer_name", param.get("engineer_name"));
		mapParam.put("node_name", param.get("node_name"));
		mapParam.put("house_type", param.get("house_type"));
		mapParam.put("house_area", param.get("house_area"));
		mapParam.put("total_price", param.get("total_price"));
		mapParam.put("house_name", param.get("house_name"));
		mapParam.put("customer_name", param.get("customer_name"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_survey_Detail", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改客户调研失败");
	}

	/** delSurveyDetail : 删除客户调研 **/
	public ResponseBodyVO delSurveyDetail(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_survey_Detail
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_survey_Detail", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除客户调研失败");
	}

}
