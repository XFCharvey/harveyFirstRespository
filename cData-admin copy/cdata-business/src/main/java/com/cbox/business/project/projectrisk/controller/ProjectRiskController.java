package com.cbox.business.project.projectrisk.controller;

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
import com.cbox.business.project.projectrisk.service.ProjectRiskService;


/**
 * @ClassName: ProjectRiskController
 * @Function: 项目风险
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectrisk")
public class ProjectRiskController extends BaseController {

	@Autowired
	private ProjectRiskService projectRiskService;

	
	/** listProjectRisk : 获取项目风险列表数据 **/
	@RequestMapping(value = "listProjectRisk", method = RequestMethod.POST)
	public TableDataInfo listProjectRisk(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectRiskService.listProjectRisk(param);

		return getDataTable(list);
	}

	/** getProjectRisk : 获取指定id的项目风险数据 **/
	@RequestMapping(value = "getProjectRisk", method = RequestMethod.POST)
	public ResponseBodyVO getProjectRisk(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectRiskService.getProjectRisk(param);
	}

	/** addProjectRisk : 新增项目风险 **/
	@RequestMapping(value = "addProjectRisk", method = RequestMethod.POST)
	public ResponseBodyVO addProjectRisk(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("project_id,risk_name,risk_level,risk_plan", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectRiskService.addProjectRisk(param);
	}

	/** updateProjectRisk : 修改项目风险 **/
	@RequestMapping(value = "updateProjectRisk", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectRisk(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectRiskService.updateProjectRisk(param);
	}

	/** delProjectRisk : 删除项目风险 **/
	@RequestMapping(value = "delProjectRisk", method = RequestMethod.POST)
	public ResponseBodyVO delProjectRisk(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectRiskService.delProjectRisk(param);
	}


}
