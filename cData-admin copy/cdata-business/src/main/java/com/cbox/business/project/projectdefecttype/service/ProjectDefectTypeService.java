package com.cbox.business.project.projectdefecttype.service;

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
import com.cbox.business.project.projectdefecttype.mapper.ProjectDefectTypeMapper;


/**
 * @ClassName: ProjectDefectTypeService
 * @Function: 项目不利因素分类组
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectDefectTypeService extends BaseService {

    @Autowired
    private ProjectDefectTypeMapper projectDefectTypeMapper;
    
	/** listProjectDefectType : 获取项目不利因素分类组列表数据 **/
	public List<Map<String, Object>> listProjectDefectType(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectDefectTypeMapper.listProjectDefectType(param);

		return list;
	}

	/** getProjectDefectType : 获取指定id的项目不利因素分类组数据 **/
	public ResponseBodyVO getProjectDefectType(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectDefectTypeMapper.getProjectDefectType(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectDefectType : 新增项目不利因素分类组 **/
	public ResponseBodyVO addProjectDefectType(Map<String, Object> param) {

		// Table:d_project_defect_type
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("defect_group", param.get("defect_group"));
		mapParam.put("defect_type_name", param.get("defect_type_name"));
		int count = this.save( "d_project_defect_type", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目不利因素分类组失败");
	}

	/** updateProjectDefectType : 修改项目不利因素分类组 **/
	public ResponseBodyVO updateProjectDefectType(Map<String, Object> param) {

		// Table:d_project_defect_type
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("defect_group", param.get("defect_group"));
		mapParam.put("defect_type_name", param.get("defect_type_name"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_defect_type", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目不利因素分类组失败"); 
	}

	/** delProjectDefectType : 删除项目不利因素分类组 **/
	public ResponseBodyVO delProjectDefectType(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_project_defect_type
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_project_defect_type",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除项目不利因素分类组失败");
	}


}
