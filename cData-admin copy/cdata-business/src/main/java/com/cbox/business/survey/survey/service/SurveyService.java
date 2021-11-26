package com.cbox.business.survey.survey.service;

import java.io.File;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.annotation.DataScope;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.poi.ExcelUtil;
import com.cbox.business.project.projectnode.bean.NodeVO;
import com.cbox.business.survey.survey.mapper.SurveyMapper;
import com.cbox.business.survey.surveydetail.bean.SurveyDetailVO;
import com.cbox.common.service.AttachFileService;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;

/**
 * @ClassName: SurveyService
 * @Function: 客户调研
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class SurveyService extends BaseService {

	@Autowired
	private SurveyMapper surveyMapper;

	@Autowired
	private AttachFileService attachFileService;

	@Autowired
	private ProjectHousesMapper projectHousesMapper;

	/** listSurvey : 获取客户调研列表数据 **/
	public List<Map<String, Object>> listSurvey(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = surveyMapper.listSurvey(param);

		return list;
	}

	/** getSurvey : 获取指定id的客户调研数据 **/
	public ResponseBodyVO getSurvey(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = surveyMapper.getSurvey(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addSurvey : 新增客户调研 **/
	public ResponseBodyVO addSurvey(Map<String, Object> param) {

		// Table:d_survey
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("survey_name", param.get("survey_name"));
		mapParam.put("survey_date", param.get("survey_date"));
		mapParam.put("file_id", param.get("file_id"));
		int count = this.save("d_survey", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增客户调研失败");
	}

	/**
	 * addSurvey : 导入文件新增客户调研
	 * 
	 * @throws Exception
	 **/
	public AjaxResult addSurveyByFile(Map<String, Object> param) throws Exception {

		// 获取上传文件地址
		String fileId = StrUtil.getMapValue(param, "file_id");
		String fileName = attachFileService.getFileRealPath(fileId);
		File file = new File(fileName);

		// Table:d_survey
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("survey_name", param.get("survey_name"));
		mapParam.put("survey_date", param.get("survey_date"));
		mapParam.put("file_id", param.get("file_id"));
		int count = this.save("d_survey", mapParam);

		// 解析excel文件
		ExcelUtil<SurveyDetailVO> util = new ExcelUtil<SurveyDetailVO>(SurveyDetailVO.class);

		List<SurveyDetailVO> importSurveyDetailList = util.importExcel(file);
		if (ObjUtil.isNotNull(importSurveyDetailList)) {
			for (int i = 0; i < importSurveyDetailList.size(); i++) {
				SurveyDetailVO surveyDetailVO = importSurveyDetailList.get(i);
				String projectName = surveyDetailVO.getProject_name();
				if (StrUtil.isNotNull(projectName)) {
					// 新增调研详情
					String surveyRecId = StrUtil.getMapValue(mapParam, "rec_id");
					addSurveyDetail(surveyDetailVO, surveyRecId, projectName);
				}
			}
		}

		// 生成返回信息
		int code = HttpStatus.SUCCESS;
		String msg = "成功导入" + count + "条客户匹配详情";
		AjaxResult ajaxResult = new AjaxResult(code, msg);

		return ajaxResult;
	}

	/** 添加調研詳情表：d_survey_detail **/
	public void addSurveyDetail(SurveyDetailVO surveyDetailVO, String surveyRecId, String projectName) {
		Map<String, Object> detailVOParam = new HashMap<String, Object>();
		detailVOParam.put("survey_id", surveyRecId);
		detailVOParam.put("send_time", surveyDetailVO.getSend_time());
		detailVOParam.put("survey_time", surveyDetailVO.getSurvey_time());
		detailVOParam.put("project_name", projectName);
		detailVOParam.put("counselor", surveyDetailVO.getAdviser_name());
		detailVOParam.put("counselor_f", surveyDetailVO.getAdviser_name_up());
		detailVOParam.put("engineer_name", surveyDetailVO.getEngineer_name());
		detailVOParam.put("node_name", surveyDetailVO.getNode_name());
		detailVOParam.put("house_type", surveyDetailVO.getHouse_type());
		detailVOParam.put("house_area", surveyDetailVO.getArea());
		detailVOParam.put("total_price", surveyDetailVO.getTotal_price());

		// 匹配客户信息
		Map<String, Object> mapInfo = getMatchInfo(detailVOParam);
		String houseName = StrUtil.getMapValue(mapInfo, "house_name");
		String customerName = StrUtil.getMapValue(mapInfo, "customer_name");
		detailVOParam.put("house_name", houseName);
		detailVOParam.put("customer_name", customerName);

		this.save("d_survey_Detail", detailVOParam);
	}

	// 匹配客户信息
	public Map<String, Object> getMatchInfo(Map<String, Object> detailVOParam) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("house_area", detailVOParam.get("house_area"));
		mapParam.put("total_price", detailVOParam.get("total_price"));
		mapParam.put("counselor", detailVOParam.get("counselor"));
		mapParam.put("project_name", detailVOParam.get("project_name"));

		List<Map<String, Object>> list = projectHousesMapper.listProjectHouses(mapParam);
		Map<String, Object> mapProHouses = null;
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				mapProHouses = list.get(i);
				String customerName = StrUtil.getMapValue(mapProHouses, "customer_name");
				if (StrUtil.isNotNull(customerName)) {
					break;
				}
			}
		}
		return mapParam;
	}

	/** updateSurvey : 修改客户调研 **/
	public ResponseBodyVO updateSurvey(Map<String, Object> param) {

		// Table:d_survey
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("survey_name", param.get("survey_name"));
		mapParam.put("survey_date", param.get("survey_date"));
		mapParam.put("file_id", param.get("file_id"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_survey", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改客户调研失败");
	}

	/** delSurvey : 删除客户调研 **/
	public ResponseBodyVO delSurvey(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_survey
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_survey", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除客户调研失败");
	}

}
