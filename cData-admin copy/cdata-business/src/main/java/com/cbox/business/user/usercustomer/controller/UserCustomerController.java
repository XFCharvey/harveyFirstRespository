package com.cbox.business.user.usercustomer.controller;

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
import com.cbox.business.user.usercustomer.service.UserCustomerService;


/**
 * @ClassName: UserCustomerController
 * @Function: 用户的客户
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/user/usercustomer")
public class UserCustomerController extends BaseController {

	@Autowired
	private UserCustomerService userCustomerService;

	
	/** listUserCustomer : 获取用户的客户列表数据 **/
	@RequestMapping(value = "listUserCustomer", method = RequestMethod.POST)
	public TableDataInfo listUserCustomer(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= userCustomerService.listUserCustomer(param);

		return getDataTable(list);
	}

	/** getUserCustomer : 获取指定id的用户的客户数据 **/
	@RequestMapping(value = "getUserCustomer", method = RequestMethod.POST)
	public ResponseBodyVO getUserCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userCustomerService.getUserCustomer(param);
	}

	/** addUserCustomer : 新增用户的客户 **/
	@RequestMapping(value = "addUserCustomer", method = RequestMethod.POST)
	public ResponseBodyVO addUserCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("follow_person,customer_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userCustomerService.addUserCustomer(param);
	}

	/** updateUserCustomer : 修改用户的客户 **/
	@RequestMapping(value = "updateUserCustomer", method = RequestMethod.POST)
	public ResponseBodyVO updateUserCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userCustomerService.updateUserCustomer(param);
	}

	/** delUserCustomer : 删除用户的客户 **/
	@RequestMapping(value = "delUserCustomer", method = RequestMethod.POST)
	public ResponseBodyVO delUserCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userCustomerService.delUserCustomer(param);
	}


}
