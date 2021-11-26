package com.cbox.business.user.userworklog.controller;

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
import com.cbox.business.user.userworklog.service.UserWorklogService;

/**
 * @ClassName: UserWorklogController
 * @Function: 员工工作日志
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/user/userworklog")
public class UserWorklogController extends BaseController {

	@Autowired
	private UserWorklogService userWorklogService;

	/** listUserWorklogByDeptId : 获取指定年月当前登陆用户团队及下级团队员工工作日志量列表数据 **/
	@RequestMapping(value = "listUserWorklogByDeptId", method = RequestMethod.POST)
	public TableDataInfo listUserWorklogByDeptId(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = userWorklogService.listUserWorklogByDeptId(param);

		return getDataTable(list);
	}

	/** listUserWorklogCountOfDay : 获取指定年月当前登陆用户团队及下级团队员工工作日志每日数量列表数据 **/
	@RequestMapping(value = "listUserWorklogCountOfDay", method = RequestMethod.POST)
	public TableDataInfo listUserWorkllistUserWorklogCountOfDayogByDeptId(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = userWorklogService.listUserWorklogCountOfDay(param);

		return getDataTable(list);
	}

	/** listUserWorklog : 获取员工工作日志列表数据 **/
	@RequestMapping(value = "listUserWorklog", method = RequestMethod.POST)
	public TableDataInfo listUserWorklog(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = userWorklogService.listUserWorklog(param);

		return getDataTable(list);
	}

	/** getUserWorklog : 获取指定id的员工工作日志数据 **/
	@RequestMapping(value = "getUserWorklog", method = RequestMethod.POST)
	public ResponseBodyVO getUserWorklog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userWorklogService.getUserWorklog(param);
	}

	/** addUserWorklog : 新增员工工作日志 **/
	@RequestMapping(value = "addUserWorklog", method = RequestMethod.POST)
	public ResponseBodyVO addUserWorklog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("log_content,log_date", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userWorklogService.addUserWorklog(param);
	}

	/** updateUserWorklog : 修改员工工作日志 **/
	@RequestMapping(value = "updateUserWorklog", method = RequestMethod.POST)
	public ResponseBodyVO updateUserWorklog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userWorklogService.updateUserWorklog(param);
	}

	/** delUserWorklog : 删除员工工作日志 **/
	@RequestMapping(value = "delUserWorklog", method = RequestMethod.POST)
	public ResponseBodyVO delUserWorklog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userWorklogService.delUserWorklog(param);
	}

}
