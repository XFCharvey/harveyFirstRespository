package com.cbox.business.project.projectbuilding.service;

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
import com.cbox.business.project.projectbuilding.mapper.ProjectBuildingMapper;


/**
 * @ClassName: ProjectBuildingService
 * @Function: 项目楼栋
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectBuildingService extends BaseService {

    @Autowired
    private ProjectBuildingMapper projectBuildingMapper;
    
	/** listProjectBuilding : 获取项目楼栋列表数据 **/
	public List<Map<String, Object>> listProjectBuilding(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectBuildingMapper.listProjectBuilding(param);

		return list;
	}

	/** getProjectBuilding : 获取指定id的项目楼栋数据 **/
	public ResponseBodyVO getProjectBuilding(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectBuildingMapper.getProjectBuilding(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectBuilding : 新增项目楼栋 **/
	public ResponseBodyVO addProjectBuilding(Map<String, Object> param) {

		// Table:d_project_building
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("building_name", param.get("building_name"));
		mapParam.put("houses_num", param.get("houses_num"));
		int count = this.save( "d_project_building", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目楼栋失败");
	}

	/** updateProjectBuilding : 修改项目楼栋 **/
	public ResponseBodyVO updateProjectBuilding(Map<String, Object> param) {

		// Table:d_project_building
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("building_name", param.get("building_name"));
		mapParam.put("houses_num", param.get("houses_num"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_building", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目楼栋失败"); 
	}

	/** delProjectBuilding : 删除项目楼栋 **/
	public ResponseBodyVO delProjectBuilding(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_project_building
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_building",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目楼栋失败");
	}


}
