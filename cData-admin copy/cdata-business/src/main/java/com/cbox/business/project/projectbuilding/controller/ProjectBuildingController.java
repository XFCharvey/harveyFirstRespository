package com.cbox.business.project.projectbuilding.controller;

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
import com.cbox.business.project.projectbuilding.service.ProjectBuildingService;


/**
 * @ClassName: ProjectBuildingController
 * @Function: 项目楼栋
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectbuilding")
public class ProjectBuildingController extends BaseController {

	@Autowired
	private ProjectBuildingService projectBuildingService;

	
	/** listProjectBuilding : 获取项目楼栋列表数据 **/
	@RequestMapping(value = "listProjectBuilding", method = RequestMethod.POST)
	public TableDataInfo listProjectBuilding(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectBuildingService.listProjectBuilding(param);

		return getDataTable(list);
	}

	/** getProjectBuilding : 获取指定id的项目楼栋数据 **/
	@RequestMapping(value = "getProjectBuilding", method = RequestMethod.POST)
	public ResponseBodyVO getProjectBuilding(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectBuildingService.getProjectBuilding(param);
	}

	/** addProjectBuilding : 新增项目楼栋 **/
	@RequestMapping(value = "addProjectBuilding", method = RequestMethod.POST)
	public ResponseBodyVO addProjectBuilding(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id,building_name,houses_num", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectBuildingService.addProjectBuilding(param);
	}

	/** updateProjectBuilding : 修改项目楼栋 **/
	@RequestMapping(value = "updateProjectBuilding", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectBuilding(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectBuildingService.updateProjectBuilding(param);
	}

	/** delProjectBuilding : 删除项目楼栋 **/
	@RequestMapping(value = "delProjectBuilding", method = RequestMethod.POST)
	public ResponseBodyVO delProjectBuilding(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectBuildingService.delProjectBuilding(param);
	}


}
