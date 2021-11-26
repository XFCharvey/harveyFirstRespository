package com.cbox.business.project.projectdefect.controller;

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
import com.cbox.business.project.projectdefect.service.ProjectDefectService;


/**
 * @ClassName: ProjectDefectController
 * @Function: 项目不利因素
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/projrct/projectdefect")
public class ProjectDefectController extends BaseController {

	@Autowired
	private ProjectDefectService projectDefectService;

	
	/** listProjectDefect : 获取项目不利因素列表数据 **/
	@RequestMapping(value = "listProjectDefect", method = RequestMethod.POST)
	public TableDataInfo listProjectDefect(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectDefectService.listProjectDefect(param);

		return getDataTable(list);
	}

	/** getProjectDefect : 获取指定id的项目不利因素数据 **/
	@RequestMapping(value = "getProjectDefect", method = RequestMethod.POST)
	public ResponseBodyVO getProjectDefect(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectService.getProjectDefect(param);
	}

	/** addProjectDefect : 新增项目不利因素 **/
	@RequestMapping(value = "addProjectDefect", method = RequestMethod.POST)
	public ResponseBodyVO addProjectDefect(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("project_id,defect_group,defect_type_id,defect_detail", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectService.addProjectDefect(param);
	}

	/** updateProjectDefect : 修改项目不利因素 **/
	@RequestMapping(value = "updateProjectDefect", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectDefect(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectService.updateProjectDefect(param);
	}

	/** delProjectDefect : 删除项目不利因素 **/
	@RequestMapping(value = "delProjectDefect", method = RequestMethod.POST)
	public ResponseBodyVO delProjectDefect(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectDefectService.delProjectDefect(param);
	}


}
