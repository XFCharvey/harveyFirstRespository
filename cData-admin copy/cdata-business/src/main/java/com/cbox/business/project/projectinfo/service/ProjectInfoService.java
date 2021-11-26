package com.cbox.business.project.projectinfo.service;

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
import com.cbox.business.project.projectinfo.mapper.ProjectInfoMapper;


/**
 * @ClassName: ProjectInfoService
 * @Function: 项目资料
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectInfoService extends BaseService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    
	/** listProjectInfo : 获取项目资料列表数据 **/
	public List<Map<String, Object>> listProjectInfo(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectInfoMapper.listProjectInfo(param);

		return list;
	}

	/** getProjectInfo : 获取指定id的项目资料数据 **/
	public ResponseBodyVO getProjectInfo(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectInfoMapper.getProjectInfo(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectInfo : 新增项目资料 **/
	public ResponseBodyVO addProjectInfo(Map<String, Object> param) {

		// Table:d_project_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("info_menu_id", param.get("info_menu_id"));
        mapParam.put("info_title", param.get("info_title"));
		mapParam.put("info_image", param.get("info_image"));
		mapParam.put("info_video", param.get("info_video"));
		mapParam.put("info_file", param.get("info_file"));
		mapParam.put("info_content", param.get("info_content"));
		int count = this.save( "d_project_info", mapParam);

        Map<String, Object> projectInfoCondition = new HashMap<String, Object>();
        projectInfoCondition.put("project_id", param.get("project_id"));
        List<Map<String, Object>> listProjectInfo = projectInfoMapper.listProjectInfo(projectInfoCondition);
        // 最新的项目资料数
        Map<String, Object> projectParam = new HashMap<String, Object>();
        projectParam.put("info_num", listProjectInfo.size());
        // 更新项目最新的资料数量
        Map<String, Object> projectCondition = new HashMap<String, Object>();
        projectCondition.put("rec_id", param.get("project_id"));
        count += this.update(projectCondition, "d_project", projectParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目资料失败");
	}

	/** updateProjectInfo : 修改项目资料 **/
	public ResponseBodyVO updateProjectInfo(Map<String, Object> param) {

		// Table:d_project_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("info_menu_id", param.get("info_menu_id"));
        mapParam.put("info_title", param.get("info_title"));
		mapParam.put("info_image", param.get("info_image"));
		mapParam.put("info_video", param.get("info_video"));
		mapParam.put("info_file", param.get("info_file"));
		mapParam.put("info_content", param.get("info_content"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目资料失败"); 
	}

	/** delProjectInfo : 删除项目资料 **/
	public ResponseBodyVO delProjectInfo(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_project_info
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        Map<String, Object> mapProjectInfo = projectInfoMapper.getProjectInfo(param);
		int count = this.delete("d_project_info",  mapCondition);

        Map<String, Object> projectInfoCondition = new HashMap<String, Object>();
        projectInfoCondition.put("project_id", mapProjectInfo.get("project_id"));
        List<Map<String, Object>> listProjectInfo = projectInfoMapper.listProjectInfo(projectInfoCondition);
        Map<String, Object> projectParam = new HashMap<String, Object>();
        if (ObjUtil.isNotNull(listProjectInfo)) {
            // 最新的项目资料数
            projectParam.put("info_num", listProjectInfo.size());
        } else {
            projectParam.put("info_num", 0);
        }
        // 更新项目最新的资料数量
        Map<String, Object> projectCondition = new HashMap<String, Object>();
        projectCondition.put("rec_id", mapProjectInfo.get("project_id"));
        count += this.update(projectCondition, "d_project", projectParam);

        return ServerRspUtil.formRspBodyVO(count, "删除项目资料失败");
	}


}
