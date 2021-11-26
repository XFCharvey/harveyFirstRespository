package com.cbox.business.project.projectcomplain.controller;

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


import com.cbox.business.project.projectcomplain.service.ProjectcomplainService;


/**
 * @ClassName: ProjectcomplainController
 * @Function: 项目投诉/维修统计
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectcomplain")
public class ProjectcomplainController extends BaseController {

	@Autowired
	private ProjectcomplainService projectcomplainService;

	
	/** listProjectcomplain : 获取项目投诉/维修统计列表数据 **/
	@RequestMapping(value = "listProjectcomplain", method = RequestMethod.POST)
	public TableDataInfo listProjectcomplain(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectcomplainService.listProjectcomplain(param);

		return getDataTable(list);
	}

	/** getProjectcomplain : 获取指定id的项目投诉/维修统计数据 **/
	@RequestMapping(value = "getProjectcomplain", method = RequestMethod.POST)
	public ResponseBodyVO getProjectcomplain(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectcomplainService.getProjectcomplain(param);
	}

	/** addProjectcomplain : 新增项目投诉/维修统计 **/
	@RequestMapping(value = "addProjectcomplain", method = RequestMethod.POST)
	public ResponseBodyVO addProjectcomplain(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id,time_type,complain_time,total_num,close_num,close_rate,satisfy_rate,reply_out_num,replay_out_rate,deal_out_num,deal_out_rate", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectcomplainService.addProjectcomplain(param);
	}

	/** updateProjectcomplain : 修改项目投诉/维修统计 **/
	@RequestMapping(value = "updateProjectcomplain", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectcomplain(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectcomplainService.updateProjectcomplain(param);
	}

	/** delProjectcomplain : 删除项目投诉/维修统计 **/
	@RequestMapping(value = "delProjectcomplain", method = RequestMethod.POST)
	public ResponseBodyVO delProjectcomplain(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectcomplainService.delProjectcomplain(param);
	}


}
