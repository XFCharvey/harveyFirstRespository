package com.cbox.business.project.projectinfo.controller;

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
import com.cbox.business.project.projectinfo.service.ProjectInfoService;


/**
 * @ClassName: ProjectInfoController
 * @Function: 项目资料
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectinfo")
public class ProjectInfoController extends BaseController {

	@Autowired
	private ProjectInfoService projectInfoService;

	
	/** listProjectInfo : 获取项目资料列表数据 **/
	@RequestMapping(value = "listProjectInfo", method = RequestMethod.POST)
	public TableDataInfo listProjectInfo(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectInfoService.listProjectInfo(param);

		return getDataTable(list);
	}

	/** getProjectInfo : 获取指定id的项目资料数据 **/
	@RequestMapping(value = "getProjectInfo", method = RequestMethod.POST)
	public ResponseBodyVO getProjectInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoService.getProjectInfo(param);
	}

	/** addProjectInfo : 新增项目资料 **/
	@RequestMapping(value = "addProjectInfo", method = RequestMethod.POST)
	public ResponseBodyVO addProjectInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("project_id,info_menu_id,info_title", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoService.addProjectInfo(param);
	}

	/** updateProjectInfo : 修改项目资料 **/
	@RequestMapping(value = "updateProjectInfo", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoService.updateProjectInfo(param);
	}

	/** delProjectInfo : 删除项目资料 **/
	@RequestMapping(value = "delProjectInfo", method = RequestMethod.POST)
	public ResponseBodyVO delProjectInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectInfoService.delProjectInfo(param);
	}


}
