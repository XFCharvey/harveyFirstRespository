package com.cbox.business.project.projectproblem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;


import com.cbox.business.project.projectproblem.service.ProjectProblemService;


/**
 * @ClassName: ProjectProblemController
 * @Function: 项目开放、交付问题
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectproblem")
public class ProjectProblemController extends BaseController {

	@Autowired
	private ProjectProblemService projectProblemService;

	
	/** listProjectProblem : 获取项目开放、交付问题列表数据 **/
	@RequestMapping(value = "listProjectProblem", method = RequestMethod.POST)
	public TableDataInfo listProjectProblem(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectProblemService.listProjectProblem(param);

		return getDataTable(list);
	}

	/** getProjectProblem : 获取指定id的项目开放、交付问题数据 **/
	@RequestMapping(value = "getProjectProblem", method = RequestMethod.POST)
	public ResponseBodyVO getProjectProblem(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectProblemService.getProjectProblem(param);
	}

	/** addProjectProblem : 新增项目开放、交付问题 **/
	@RequestMapping(value = "addProjectProblem", method = RequestMethod.POST)
	public ResponseBodyVO addProjectProblem(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("problem_code,proj_guid,batch_id,type,room_id,room_name,position_name,problem_descr,contractor_name,emergency_degree,status,regist_person,regist_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectProblemService.addProjectProblem(param);
	}

	/** updateProjectProblem : 修改项目开放、交付问题 **/
	@RequestMapping(value = "updateProjectProblem", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectProblem(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectProblemService.updateProjectProblem(param);
	}

	/** delProjectProblem : 删除项目开放、交付问题 **/
	@RequestMapping(value = "delProjectProblem", method = RequestMethod.POST)
	public ResponseBodyVO delProjectProblem(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectProblemService.delProjectProblem(param);
	}


}
