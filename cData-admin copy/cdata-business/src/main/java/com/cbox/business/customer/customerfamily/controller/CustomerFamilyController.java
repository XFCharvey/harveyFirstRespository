package com.cbox.business.customer.customerfamily.controller;

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
import com.cbox.business.customer.customerfamily.service.CustomerFamilyService;


/**
 * @ClassName: CustomerFamilyController
 * @Function: 客户家庭成员
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customerfamily")
public class CustomerFamilyController extends BaseController {

	@Autowired
	private CustomerFamilyService customerFamilyService;

	
	/** listCustomerFamily : 获取客户家庭成员列表数据 **/
	@RequestMapping(value = "listCustomerFamily", method = RequestMethod.POST)
	public TableDataInfo listCustomerFamily(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= customerFamilyService.listCustomerFamily(param);

		return getDataTable(list);
	}

	/** getCustomerFamily : 获取指定id的客户家庭成员数据 **/
	@RequestMapping(value = "getCustomerFamily", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerFamily(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyService.getCustomerFamily(param);
	}

	/** addCustomerFamily : 新增客户家庭成员 **/
	@RequestMapping(value = "addCustomerFamily", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerFamily(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("customer_id,family_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyService.addCustomerFamily(param);
	}

	/** updateCustomerFamily : 修改客户家庭成员 **/
	@RequestMapping(value = "updateCustomerFamily", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomerFamily(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyService.updateCustomerFamily(param);
	}

	/** delCustomerFamily : 删除客户家庭成员 **/
	@RequestMapping(value = "delCustomerFamily", method = RequestMethod.POST)
	public ResponseBodyVO delCustomerFamily(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerFamilyService.delCustomerFamily(param);
	}


}
