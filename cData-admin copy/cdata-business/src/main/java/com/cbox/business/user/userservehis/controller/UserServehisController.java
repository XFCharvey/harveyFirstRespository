package com.cbox.business.user.userservehis.controller;

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
import com.cbox.business.user.userservehis.service.UserServehisService;


/**
 * @ClassName: UserServehisController
 * @Function: 员工用户服务历史
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/user/userservehis")
public class UserServehisController extends BaseController {

	@Autowired
	private UserServehisService userServehisService;

	
	/** listUserServehis : 获取员工用户服务历史列表数据 **/
	@RequestMapping(value = "listUserServehis", method = RequestMethod.POST)
	public TableDataInfo listUserServehis(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= userServehisService.listUserServehis(param);

		return getDataTable(list);
	}

	/** getUserServehis : 获取指定id的员工用户服务历史数据 **/
	@RequestMapping(value = "getUserServehis", method = RequestMethod.POST)
	public ResponseBodyVO getUserServehis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userServehisService.getUserServehis(param);
	}

	/** addUserServehis : 新增员工用户服务历史 **/
	@RequestMapping(value = "addUserServehis", method = RequestMethod.POST)
	public ResponseBodyVO addUserServehis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("served_customer", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userServehisService.addUserServehis(param);
	}

	/** updateUserServehis : 修改员工用户服务历史 **/
	@RequestMapping(value = "updateUserServehis", method = RequestMethod.POST)
	public ResponseBodyVO updateUserServehis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userServehisService.updateUserServehis(param);
	}

	/** delUserServehis : 删除员工用户服务历史 **/
	@RequestMapping(value = "delUserServehis", method = RequestMethod.POST)
	public ResponseBodyVO delUserServehis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return userServehisService.delUserServehis(param);
	}


}
