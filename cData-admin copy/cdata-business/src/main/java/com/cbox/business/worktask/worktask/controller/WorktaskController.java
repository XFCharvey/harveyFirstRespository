package com.cbox.business.worktask.worktask.controller;

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
import com.cbox.business.worktask.worktask.service.WorktaskService;

/**
 * @ClassName: WorktaskController
 * @Function: 任务表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/worktask")
public class WorktaskController extends BaseController {

	@Autowired
	private WorktaskService worktaskService;

	/** listWorktaskCountOfUser : 获取预警任务的用户及工作任务数量 **/
	@RequestMapping(value = "listWorktaskCountOfUser", method = RequestMethod.POST)
	public TableDataInfo listWorktaskCountOfUser(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = worktaskService.listWorktaskCountOfUser(param);

		return getDataTable(list);
	}

	/** exportWorktasks : 导出任务表列表数据excel文件 **/
	@RequestMapping(value = "exportWorktasks", method = RequestMethod.POST)
	public AjaxResult exportWorktasks(@RequestBody Map<String, Object> param) {
		return worktaskService.exportWorktasks(param);
	}

	/** getWorktaskTypeGroup : 统计任务工单类型比例分布 **/
	@RequestMapping(value = "getWorktaskTypeGroup", method = RequestMethod.POST)
	public ResponseBodyVO getWorktaskTypeGroup(@RequestBody Map<String, Object> param) {

		return worktaskService.getWorktaskTypeGroup(param);
	}

	/** listWorktask : 获取任务表列表数据 **/
	@RequestMapping(value = "listWorktask", method = RequestMethod.POST)
	public TableDataInfo listWorktask(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = worktaskService.listWorktask(param);

		return getDataTable(list);
	}

	/** listOverdueWorktask : 获取预警（完成期限于今日差距小于5）任务表列表数据 **/
	@RequestMapping(value = "listOverdueWorktask", method = RequestMethod.POST)
	public TableDataInfo listOverdueWorktask(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = worktaskService.listOverdueWorktask(param);

		return getDataTable(list);
	}

	/** 获取抄送人包含当前用户的任务 **/
	@RequestMapping(value = "listWorktaskCopyPerson", method = RequestMethod.POST)
	public TableDataInfo listWorktaskCopyPerson(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = worktaskService.listWorktaskCopyPerson(param);

		return getDataTable(list);
	}

	/** getSubTaskList : 获取指定id派生的子任务数据 **/
	@RequestMapping(value = "getSubTaskList", method = RequestMethod.POST)
	public ResponseBodyVO getSubTaskList(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("relate_taskid", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return worktaskService.getSubTaskList(param);
	}

	/** getWorktask : 获取指定id的任务表数据 **/
	@RequestMapping(value = "getWorktask", method = RequestMethod.POST)
	public ResponseBodyVO getWorktask(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return worktaskService.getWorktask(param);
	}

	/** addWorktask : 新增任务表 **/
	@RequestMapping(value = "addWorktask", method = RequestMethod.POST)
	public ResponseBodyVO addWorktask(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id,task_name,task_type", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return worktaskService.addWorktask(param);
	}

	/** updateWorktask : 修改任务表 **/
	@RequestMapping(value = "updateWorktask", method = RequestMethod.POST)
	public ResponseBodyVO updateWorktask(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return worktaskService.updateWorktask(param);
	}

	/** delWorktask : 删除任务表 **/
	@RequestMapping(value = "delWorktask", method = RequestMethod.POST)
	public ResponseBodyVO delWorktask(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return worktaskService.delWorktask(param);
	}

}