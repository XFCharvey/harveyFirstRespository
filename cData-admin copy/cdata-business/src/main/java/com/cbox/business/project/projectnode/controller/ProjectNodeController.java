package com.cbox.business.project.projectnode.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.project.projectnode.service.ProjectNodeService;

/**
 * @ClassName: ProjectNodeController
 * @Function: 项目节点
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/projrct/projectnode")
public class ProjectNodeController extends BaseController {

	@Autowired
	private ProjectNodeService projectNodeService;

	/** listProjectNode : 获取项目节点列表数据 **/
	@RequestMapping(value = "listProjectNode", method = RequestMethod.POST)
	public TableDataInfo listProjectNode(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = projectNodeService.listProjectNode(param);

		return getDataTable(list);
	}

	/** getDownloadTemplate : 获取项目批量节点导入下载模板 **/
	@RequestMapping(value = "getDownloadTemplate", method = RequestMethod.POST)
	public AjaxResult getDownloadTemplate(@RequestBody Map<String, Object> param) {
		return projectNodeService.getDownloadTemplate();
	}

	/** getProjectNode : 获取指定id的项目节点数据 **/
	@RequestMapping(value = "getProjectNode", method = RequestMethod.POST)
	public ResponseBodyVO getProjectNode(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeService.getProjectNode(param);
	}

	/** addProjectNode : 新增项目节点 **/
	@RequestMapping(value = "addProjectNode", method = RequestMethod.POST)
	public ResponseBodyVO addProjectNode(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id,node_name,node_deadline", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeService.addProjectNode(param);
	}

	/**
	 * addProjectNode : 批量导入项目节点
	 * 
	 * @throws Exception
	 **/
	@RequestMapping(value = "importProjectNode", method = RequestMethod.POST)
	public AjaxResult importProjectNode(@RequestBody Map<String, Object> param) throws Exception {

		// 校验必填参数
		String checkResult = this.validInput("project_id,node_file", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return AjaxResult.error(checkResult);
		}

		return projectNodeService.importProjectNode(param);
	}

	/**
	 * updateProjectNode : 修改项目节点
	 * 
	 * @throws ParseException
	 **/
	@RequestMapping(value = "updateProjectNode", method = RequestMethod.POST)
	public ResponseBodyVO updateProjectNode(@RequestBody Map<String, Object> param) throws ParseException {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeService.updateProjectNode(param);
	}

	/** delProjectNode : 删除项目节点 **/
	@RequestMapping(value = "delProjectNode", method = RequestMethod.POST)
	public ResponseBodyVO delProjectNode(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id,project_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return projectNodeService.delProjectNode(param);
	}

}
