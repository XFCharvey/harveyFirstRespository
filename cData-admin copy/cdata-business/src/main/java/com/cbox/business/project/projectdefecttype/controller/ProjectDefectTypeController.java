package com.cbox.business.project.projectdefecttype.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.project.projectdefecttype.service.ProjectDefectTypeService;


/**
 * @ClassName: ProjectDefectTypeController
 * @Function: 项目不利因素分类组
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/projrct/projectdefecttype")
public class ProjectDefectTypeController extends BaseController {

	@Autowired
	private ProjectDefectTypeService projectDefectTypeService;

	
	/** listProjectDefectType : 获取项目不利因素分类组列表数据 **/
	@RequestMapping(value = "listProjectDefectType", method = RequestMethod.POST)
	public TableDataInfo listProjectDefectType(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectDefectTypeService.listProjectDefectType(param);

		return getDataTable(list);
	}

	/** getProjectDefectType : 获取指定id的项目不利因素分类组数据 **/
	@RequestMapping(value = "getProjectDefectType", method = RequestMethod.POST)
	public ResponseBodyVO getProjectDefectType(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectTypeService.getProjectDefectType(param);
	}

	/** addProjectDefectType : 新增项目不利因素分类组 **/
	@RequestMapping(value = "addProjectDefectType", method = RequestMethod.POST)
	public ResponseBodyVO addProjectDefectType(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("defect_group,defect_type_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectTypeService.addProjectDefectType(param);
	}

	/** updateProjectDefectType : 修改项目不利因素分类组 **/
	@RequestMapping(value = "updateProjectDefectType", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectDefectType(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectTypeService.updateProjectDefectType(param);
	}

	/** delProjectDefectType : 删除项目不利因素分类组 **/
	@RequestMapping(value = "delProjectDefectType", method = RequestMethod.POST)
	public ResponseBodyVO delProjectDefectType(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectTypeService.delProjectDefectType(param);
	}


}
