package com.cbox.business.project.projectnodedelayhis.controller;

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


import com.cbox.business.project.projectnodedelayhis.service.ProjectNodeDelayHisService;


/**
 * @ClassName: ProjectNodeDelayHisController
 * @Function: 项目节点延期
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/projectnodedelayhis")
public class ProjectNodeDelayHisController extends BaseController {

	@Autowired
	private ProjectNodeDelayHisService projectNodeDelayHisService;

	
	/** listProjectNodeDelayHis : 获取项目节点延期列表数据 **/
	@RequestMapping(value = "listProjectNodeDelayHis", method = RequestMethod.POST)
	public TableDataInfo listProjectNodeDelayHis(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= projectNodeDelayHisService.listProjectNodeDelayHis(param);

		return getDataTable(list);
	}

	/** getProjectNodeDelayHis : 获取指定id的项目节点延期数据 **/
	@RequestMapping(value = "getProjectNodeDelayHis", method = RequestMethod.POST)
	public ResponseBodyVO getProjectNodeDelayHis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeDelayHisService.getProjectNodeDelayHis(param);
	}

	/** addProjectNodeDelayHis : 新增项目节点延期 **/
	@RequestMapping(value = "addProjectNodeDelayHis", method = RequestMethod.POST)
	public ResponseBodyVO addProjectNodeDelayHis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("node_id,delay_deadline,node_deadline,delay_reason", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeDelayHisService.addProjectNodeDelayHis(param);
	}

	/** updateProjectNodeDelayHis : 修改项目节点延期 **/
	@RequestMapping(value = "updateProjectNodeDelayHis", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectNodeDelayHis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeDelayHisService.updateProjectNodeDelayHis(param);
	}

	/** delProjectNodeDelayHis : 删除项目节点延期 **/
	@RequestMapping(value = "delProjectNodeDelayHis", method = RequestMethod.POST)
	public ResponseBodyVO delProjectNodeDelayHis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeDelayHisService.delProjectNodeDelayHis(param);
	}


}
