package com.cbox.business.project.projectrisk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.projectrisk.mapper.ProjectRiskMapper;


/**
 * @ClassName: ProjectRiskService
 * @Function: 项目风险
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectRiskService extends BaseService {

    @Autowired
    private ProjectRiskMapper projectRiskMapper;
    
	/** listProjectRisk : 获取项目风险列表数据 **/
	public List<Map<String, Object>> listProjectRisk(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectRiskMapper.listProjectRisk(param);

		return list;
	}

	/** getProjectRisk : 获取指定id的项目风险数据 **/
	public ResponseBodyVO getProjectRisk(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectRiskMapper.getProjectRisk(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectRisk : 新增项目风险 **/
	public ResponseBodyVO addProjectRisk(Map<String, Object> param) {
        int count = 0;
		// Table:d_project_risk
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("risk_name", param.get("risk_name"));
		mapParam.put("risk_level", param.get("risk_level"));
		mapParam.put("risk_status", param.get("risk_status"));
		mapParam.put("risk_plan", param.get("risk_plan"));
        count += this.save("d_project_risk", mapParam);
        // 风险查询条件
        Map<String, Object> mapProjectRiskCondition = new HashMap<String, Object>();
        mapProjectRiskCondition.put("project_id", param.get("project_id"));
        // 查询风险数据
        List<Map<String, Object>> listRiskCondition = projectRiskMapper.listProjectRisk(mapProjectRiskCondition);

        // 最新的项目风险数
        Map<String, Object> projectParam = new HashMap<String, Object>();
        projectParam.put("risk_num", listRiskCondition.size());
        // 更新项目最新的风险数
        Map<String, Object> projectCondition = new HashMap<String, Object>();
        projectCondition.put("rec_id", param.get("project_id"));
        count += this.update(projectCondition, "d_project", projectParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目风险失败");
	}

	/** updateProjectRisk : 修改项目风险 **/
	public ResponseBodyVO updateProjectRisk(Map<String, Object> param) {

		// Table:d_project_risk
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("risk_name", param.get("risk_name"));
		mapParam.put("risk_level", param.get("risk_level"));
		mapParam.put("risk_status", param.get("risk_status"));
		mapParam.put("risk_plan", param.get("risk_plan"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_risk", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目风险失败"); 
	}

	/** delProjectRisk : 删除项目风险 **/
	public ResponseBodyVO delProjectRisk(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 
        int count = 0;
		// Table:d_project_risk
		Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        Map<String, Object> mapProjectRisk = projectRiskMapper.getProjectRisk(param);
        String projectID = StrUtil.getMapValue(mapProjectRisk, "project_id");
        count = this.delete("d_project_risk", mapCondition);
        /** 对应项目风险数量更新 **/
        // 风险查询条件
        Map<String, Object> mapProjectRiskCondition = new HashMap<String, Object>();
        mapProjectRiskCondition.put("project_id", projectID);
        // 查询风险数据
        List<Map<String, Object>> listRiskCondition = projectRiskMapper.listProjectRisk(mapProjectRiskCondition);
        Map<String, Object> projectParam = new HashMap<String, Object>();
        if (ObjUtil.isNotNull(listRiskCondition)) {
            // 最新的项目风险数
            projectParam.put("risk_num", listRiskCondition.size());
        } else {
            projectParam.put("risk_num", 0);
        }
        // 更新项目最新的风险数
        Map<String, Object> projectCondition = new HashMap<String, Object>();
        projectCondition.put("rec_id", projectID);
        count += this.update(projectCondition, "d_project", projectParam);

		return ServerRspUtil.formRspBodyVO(count, "删除项目风险失败");
	}


}
