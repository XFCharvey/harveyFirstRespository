package com.cbox.business.customer.customerfamilylive.controller;

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
import com.cbox.business.customer.customerfamilylive.service.CustomerFamilyliveService;


/**
 * @ClassName: CustomerFamilyliveController
 * @Function: 客户家庭居住现状
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customerfamilylive")
public class CustomerFamilyliveController extends BaseController {

	@Autowired
	private CustomerFamilyliveService customerFamilyliveService;

	
	/** listCustomerFamilylive : 获取客户家庭居住现状列表数据 **/
	@RequestMapping(value = "listCustomerFamilylive", method = RequestMethod.POST)
	public TableDataInfo listCustomerFamilylive(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= customerFamilyliveService.listCustomerFamilylive(param);

		return getDataTable(list);
	}

	/** getCustomerFamilylive : 获取指定id的客户家庭居住现状数据 **/
	@RequestMapping(value = "getCustomerFamilylive", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerFamilylive(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyliveService.getCustomerFamilylive(param);
	}

	/** addCustomerFamilylive : 新增客户家庭居住现状 **/
	@RequestMapping(value = "addCustomerFamilylive", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerFamilylive(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("customer_id,live_status", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyliveService.addCustomerFamilylive(param);
	}

	/** updateCustomerFamilylive : 修改客户家庭居住现状 **/
	@RequestMapping(value = "updateCustomerFamilylive", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomerFamilylive(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyliveService.updateCustomerFamilylive(param);
	}

	/** delCustomerFamilylive : 删除客户家庭居住现状 **/
	@RequestMapping(value = "delCustomerFamilylive", method = RequestMethod.POST)
	public ResponseBodyVO delCustomerFamilylive(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyliveService.delCustomerFamilylive(param);
	}


}
