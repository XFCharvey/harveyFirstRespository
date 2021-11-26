package com.cbox.business.project.projectproblem.service;

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

import com.cbox.business.project.projectproblem.mapper.ProjectProblemMapper;


/**
 * @ClassName: ProjectProblemService
 * @Function: 项目开放、交付问题
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectProblemService extends BaseService {

    @Autowired
    private ProjectProblemMapper projectProblemMapper;
    
	/** listProjectProblem : 获取项目开放、交付问题列表数据 **/
	public List<Map<String, Object>> listProjectProblem(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectProblemMapper.listProjectProblem(param);

		return list;
	}

	/** getProjectProblem : 获取指定id的项目开放、交付问题数据 **/
	public ResponseBodyVO getProjectProblem(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectProblemMapper.getProjectProblem(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectProblem : 新增项目开放、交付问题 **/
	public ResponseBodyVO addProjectProblem(Map<String, Object> param) {

		// Table:d_project_problem
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("problem_code", param.get("problem_code"));
		mapParam.put("proj_guid", param.get("proj_guid"));
		mapParam.put("batch_id", param.get("batch_id"));
		mapParam.put("type", param.get("type"));
		mapParam.put("room_id", param.get("room_id"));
		mapParam.put("room_name", param.get("room_name"));
		mapParam.put("position_name", param.get("position_name"));
		mapParam.put("problem_descr", param.get("problem_descr"));
		mapParam.put("contractor_name", param.get("contractor_name"));
		mapParam.put("emergency_degree", param.get("emergency_degree"));
		mapParam.put("status", param.get("status"));
		mapParam.put("regist_person", param.get("regist_person"));
		mapParam.put("regist_time", param.get("regist_time"));
		int count = this.save( "d_project_problem", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目开放、交付问题失败");
	}

	/** updateProjectProblem : 修改项目开放、交付问题 **/
	public ResponseBodyVO updateProjectProblem(Map<String, Object> param) {

		// Table:d_project_problem
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("problem_code", param.get("problem_code"));
		mapParam.put("proj_guid", param.get("proj_guid"));
		mapParam.put("batch_id", param.get("batch_id"));
		mapParam.put("type", param.get("type"));
		mapParam.put("room_id", param.get("room_id"));
		mapParam.put("room_name", param.get("room_name"));
		mapParam.put("position_name", param.get("position_name"));
		mapParam.put("problem_descr", param.get("problem_descr"));
		mapParam.put("contractor_name", param.get("contractor_name"));
		mapParam.put("emergency_degree", param.get("emergency_degree"));
		mapParam.put("status", param.get("status"));
		mapParam.put("regist_person", param.get("regist_person"));
		mapParam.put("regist_time", param.get("regist_time"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_problem", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目开放、交付问题失败"); 
	}

	/** delProjectProblem : 删除项目开放、交付问题 **/
	public ResponseBodyVO delProjectProblem(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_project_problem
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_problem",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目开放、交付问题失败");
	}


}
