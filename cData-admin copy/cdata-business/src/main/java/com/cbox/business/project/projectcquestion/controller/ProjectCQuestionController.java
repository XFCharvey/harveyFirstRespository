package com.cbox.business.project.projectcquestion.controller;

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


import com.cbox.business.project.projectcquestion.service.ProjectCQuestionService;


/**
 * @ClassName: ProjectCQuestionController
 * @Function: 项目投诉/维修的存在问题
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectcquestion")
public class ProjectCQuestionController extends BaseController {

	@Autowired
	private ProjectCQuestionService projectCQuestionService;

	
	/** listProjectCQuestion : 获取项目投诉/维修的存在问题列表数据 **/
	@RequestMapping(value = "listProjectCQuestion", method = RequestMethod.POST)
	public TableDataInfo listProjectCQuestion(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectCQuestionService.listProjectCQuestion(param);

		return getDataTable(list);
	}

	/** getProjectCQuestion : 获取指定id的项目投诉/维修的存在问题数据 **/
	@RequestMapping(value = "getProjectCQuestion", method = RequestMethod.POST)
	public ResponseBodyVO getProjectCQuestion(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectCQuestionService.getProjectCQuestion(param);
	}

	/** addProjectCQuestion : 新增项目投诉/维修的存在问题 **/
	@RequestMapping(value = "addProjectCQuestion", method = RequestMethod.POST)
	public ResponseBodyVO addProjectCQuestion(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("complain_id,question_type,question_name,num,rate", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectCQuestionService.addProjectCQuestion(param);
	}

	/** updateProjectCQuestion : 修改项目投诉/维修的存在问题 **/
	@RequestMapping(value = "updateProjectCQuestion", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectCQuestion(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectCQuestionService.updateProjectCQuestion(param);
	}

	/** delProjectCQuestion : 删除项目投诉/维修的存在问题 **/
	@RequestMapping(value = "delProjectCQuestion", method = RequestMethod.POST)
	public ResponseBodyVO delProjectCQuestion(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectCQuestionService.delProjectCQuestion(param);
	}


}
