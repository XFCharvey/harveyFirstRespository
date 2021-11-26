package com.cbox.business.project.projectcquestion.service;

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

import com.cbox.business.project.projectcquestion.mapper.ProjectCQuestionMapper;


/**
 * @ClassName: ProjectCQuestionService
 * @Function: 项目投诉/维修的存在问题
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectCQuestionService extends BaseService {

    @Autowired
    private ProjectCQuestionMapper projectCQuestionMapper;
    
	/** listProjectCQuestion : 获取项目投诉/维修的存在问题列表数据 **/
	public List<Map<String, Object>> listProjectCQuestion(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectCQuestionMapper.listProjectCQuestion(param);

		return list;
	}

	/** getProjectCQuestion : 获取指定id的项目投诉/维修的存在问题数据 **/
	public ResponseBodyVO getProjectCQuestion(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectCQuestionMapper.getProjectCQuestion(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectCQuestion : 新增项目投诉/维修的存在问题 **/
	public ResponseBodyVO addProjectCQuestion(Map<String, Object> param) {

		// Table:d_project_complain_question
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("complain_id", param.get("complain_id"));
		mapParam.put("question_type", param.get("question_type"));
		mapParam.put("question_name", param.get("question_name"));
		mapParam.put("num", param.get("num"));
		mapParam.put("rate", param.get("rate"));
		int count = this.save( "d_project_complain_question", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目投诉/维修的存在问题失败");
	}

	/** updateProjectCQuestion : 修改项目投诉/维修的存在问题 **/
	public ResponseBodyVO updateProjectCQuestion(Map<String, Object> param) {

		// Table:d_project_complain_question
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("complain_id", param.get("complain_id"));
		mapParam.put("question_type", param.get("question_type"));
		mapParam.put("question_name", param.get("question_name"));
		mapParam.put("num", param.get("num"));
		mapParam.put("rate", param.get("rate"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_complain_question", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目投诉/维修的存在问题失败"); 
	}

	/** delProjectCQuestion : 删除项目投诉/维修的存在问题 **/
	public ResponseBodyVO delProjectCQuestion(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_project_complain_question
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_complain_question",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目投诉/维修的存在问题失败");
	}


}
