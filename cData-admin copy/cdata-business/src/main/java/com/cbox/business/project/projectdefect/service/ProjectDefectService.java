package com.cbox.business.project.projectdefect.service;

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
import com.cbox.business.project.projectdefect.mapper.ProjectDefectMapper;


/**
 * @ClassName: ProjectDefectService
 * @Function: 项目不利因素
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ProjectDefectService extends BaseService {

    @Autowired
    private ProjectDefectMapper projectDefectMapper;
    
	/** listProjectDefect : 获取项目不利因素列表数据 **/
	public List<Map<String, Object>> listProjectDefect(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = projectDefectMapper.listProjectDefect(param);

		return list;
	}

	/** getProjectDefect : 获取指定id的项目不利因素数据 **/
	public ResponseBodyVO getProjectDefect(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = projectDefectMapper.getProjectDefect(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addProjectDefect : 新增项目不利因素 **/
	public ResponseBodyVO addProjectDefect(Map<String, Object> param) {
        int count = 0;
		// Table:d_project_defect
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("defect_group", param.get("defect_group"));
		mapParam.put("defect_type_id", param.get("defect_type_id"));
		mapParam.put("defect_image", param.get("defect_image"));
		mapParam.put("defect_detail", param.get("defect_detail"));
        count += this.save("d_project_defect", mapParam);

        String defectGroup = StrUtil.getMapValue(param, "defect_group");

        Map<String, Object> defectCondition = new HashMap<String, Object>();
        defectCondition.put("project_id", param.get("project_id"));
        defectCondition.put("defect_group", param.get("defect_group"));
        List<Map<String, Object>> listDefect = projectDefectMapper.listProjectDefect(defectCondition);

        Map<String, Object> projectParam = new HashMap<String, Object>();
        if ("out".equals(defectGroup)) {
            projectParam.put("redLine_out_defect", listDefect.size());
        } else if ("in".equals(defectGroup)) {
            projectParam.put("redLine_in_defect", listDefect.size());
        } else {
            projectParam.put("difference_num", listDefect.size());
        }

        Map<String, Object> projectCondition = new HashMap<String, Object>();
        projectCondition.put("rec_id", param.get("project_id"));
        count += this.update(projectCondition, "d_project", projectParam);

		return ServerRspUtil.formRspBodyVO(count, "新增项目不利因素失败");
	}

	/** updateProjectDefect : 修改项目不利因素 **/
	public ResponseBodyVO updateProjectDefect(Map<String, Object> param) {

		// Table:d_project_defect
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("project_id", param.get("project_id"));
		mapParam.put("defect_group", param.get("defect_group"));
		mapParam.put("defect_type_id", param.get("defect_type_id"));
		mapParam.put("defect_image", param.get("defect_image"));
		mapParam.put("defect_detail", param.get("defect_detail"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_project_defect", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改项目不利因素失败"); 
	}

	/** delProjectDefect : 删除项目不利因素 **/
	public ResponseBodyVO delProjectDefect(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 
        int count = 0;
		// Table:d_project_defect
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        Map<String, Object> mapProjectDefect = projectDefectMapper.getProjectDefect(param);

        count += this.delete("d_project_defect", mapCondition);

        String defectGroup = StrUtil.getMapValue(mapProjectDefect, "defect_group");

        Map<String, Object> defectCondition = new HashMap<String, Object>();
        defectCondition.put("project_id", mapProjectDefect.get("project_id"));
        defectCondition.put("defect_group", mapProjectDefect.get("defect_group"));
        List<Map<String, Object>> listDefect = projectDefectMapper.listProjectDefect(defectCondition);
        Map<String, Object> projectParam = new HashMap<String, Object>();
        if (ObjUtil.isNotNull(listDefect)) {
            if ("out".equals(defectGroup)) {
                projectParam.put("redLine_out_defect", listDefect.size());
            } else if ("in".equals(defectGroup)) {
                projectParam.put("redLine_in_defect", listDefect.size());
            } else {
                projectParam.put("difference_num", listDefect.size());
            }
        } else {
            if ("out".equals(defectGroup)) {
                projectParam.put("redLine_out_defect", 0);
            } else if ("in".equals(defectGroup)) {
                projectParam.put("redLine_in_defect", 0);
            } else {
                projectParam.put("difference_num", 0);
            }
        }

        Map<String, Object> projectCondition = new HashMap<String, Object>();
        projectCondition.put("rec_id", mapProjectDefect.get("project_id"));
        count += this.update(projectCondition, "d_project", projectParam);
		return ServerRspUtil.formRspBodyVO(count, "删除项目不利因素失败");
	}


}
