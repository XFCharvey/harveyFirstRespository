package com.cbox.business.user.userremind.controller;

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
import com.cbox.business.user.userremind.service.UserRemindService;


/**
 * @ClassName: UserRemindController
 * @Function: 员工个人提醒
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/user/userremind")
public class UserRemindController extends BaseController {

	@Autowired
	private UserRemindService userRemindService;

	
	/** listUserRemind : 获取员工个人提醒列表数据 **/
	@RequestMapping(value = "listUserRemind", method = RequestMethod.POST)
	public TableDataInfo listUserRemind(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= userRemindService.listUserRemind(param);

		return getDataTable(list);
	}

	/** getUserRemind : 获取指定id的员工个人提醒数据 **/
	@RequestMapping(value = "getUserRemind", method = RequestMethod.POST)
	public ResponseBodyVO getUserRemind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userRemindService.getUserRemind(param);
	}

	/** addUserRemind : 新增员工个人提醒 **/
	@RequestMapping(value = "addUserRemind", method = RequestMethod.POST)
	public ResponseBodyVO addUserRemind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("remind_title,remind_time,remind_desc", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userRemindService.addUserRemind(param);
	}

	/** updateUserRemind : 修改员工个人提醒 **/
	@RequestMapping(value = "updateUserRemind", method = RequestMethod.POST)
	public ResponseBodyVO updateUserRemind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userRemindService.updateUserRemind(param);
	}

	/** delUserRemind : 删除员工个人提醒 **/
	@RequestMapping(value = "delUserRemind", method = RequestMethod.POST)
	public ResponseBodyVO delUserRemind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userRemindService.delUserRemind(param);
	}


}
