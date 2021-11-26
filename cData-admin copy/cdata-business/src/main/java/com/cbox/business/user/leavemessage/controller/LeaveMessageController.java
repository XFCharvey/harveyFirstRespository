package com.cbox.business.user.leavemessage.controller;

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
import com.cbox.business.user.leavemessage.service.LeaveMessageService;


/**
 * @ClassName: LeaveMessageController
 * @Function: 用户app咨询留言
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/user/leavemessage")
public class LeaveMessageController extends BaseController {

	@Autowired
	private LeaveMessageService leaveMessageService;

	
	/** listLeaveMessage : 获取用户app咨询留言列表数据 **/
    @RequestMapping(value = "listLeaveMessageByUser", method = RequestMethod.POST)
    public TableDataInfo listLeaveMessageByUser(@RequestBody Map<String, Object> param) {

		startPage();
        List<Map<String, Object>> list = leaveMessageService.listLeaveMessage(param, true);

		return getDataTable(list);
	}

    /** listLeaveMessage : 获取所有用户app咨询留言列表数据 **/
    @RequestMapping(value = "listLeaveMessage", method = RequestMethod.POST)
    public TableDataInfo listLeaveMessage(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = leaveMessageService.listLeaveMessage(param, false);

        return getDataTable(list);
    }

	/** getLeaveMessage : 获取指定id的用户app咨询留言数据 **/
	@RequestMapping(value = "getLeaveMessage", method = RequestMethod.POST)
	public ResponseBodyVO getLeaveMessage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return leaveMessageService.getLeaveMessage(param);
	}

	/** addLeaveMessage : 新增用户app咨询留言 **/
	@RequestMapping(value = "addLeaveMessage", method = RequestMethod.POST)
	public ResponseBodyVO addLeaveMessage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("leave_message", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return leaveMessageService.addLeaveMessage(param);
	}

	/** updateLeaveMessage : 修改用户app咨询留言 **/
	@RequestMapping(value = "updateLeaveMessage", method = RequestMethod.POST)
	public ResponseBodyVO updateLeaveMessage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return leaveMessageService.updateLeaveMessage(param);
	}

	/** delLeaveMessage : 删除用户app咨询留言 **/
	@RequestMapping(value = "delLeaveMessage", method = RequestMethod.POST)
	public ResponseBodyVO delLeaveMessage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return leaveMessageService.delLeaveMessage(param);
	}


}
