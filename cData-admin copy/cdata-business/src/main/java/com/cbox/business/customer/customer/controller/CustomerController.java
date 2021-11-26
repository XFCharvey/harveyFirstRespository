package com.cbox.business.customer.customer.controller;

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
import com.cbox.business.customer.customer.service.CustomerService;

/**
 * @ClassName: CustomerController
 * @Function: 客户信息
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer")
public class CustomerController extends BaseController {

	@Autowired
	private CustomerService customerService;

	/** listCustomer : 获取客户信息列表数据 **/
	@RequestMapping(value = "listCustomer", method = RequestMethod.POST)
	public TableDataInfo listCustomer(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = customerService.listCustomer(param);

		return getDataTable(list);
	}
	
	/** listCustomerOfHouse : 获取导出客户信息列表数据 **/
	@RequestMapping(value = "listCustomerOfHouse", method = RequestMethod.POST)
	public TableDataInfo listCustomerOfHouse(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = customerService.listCustomerOfHouse(param);

		return getDataTable(list);
	}
	
	/** exportCustomerOfHouse : 导出客户与房间信息列表excel文件 **/
	@RequestMapping(value = "exportCustomerOfHouse", method = RequestMethod.POST)
	public AjaxResult exportCustomerOfHouse(@RequestBody Map<String, Object> param) {
		return customerService.exportCustomerOfHouse(param);
	}

	/** listCustomerApp : 获取客户信息列表数据 **/
	@RequestMapping(value = "listCustomerApp", method = RequestMethod.POST)
	public TableDataInfo listCustomerApp(@RequestBody Map<String, Object> param) {
		
		startPage();
		List<Map<String, Object>> list = customerService.listCustomerApp(param);

		return getDataTable(list);
	}

	/** getCustomer : 获取指定id的客户信息数据 **/
	@RequestMapping(value = "getCustomer", method = RequestMethod.POST)
	public ResponseBodyVO getCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerService.getCustomer(param);
	}

	/** getCustomerOtherTotal : 获取指定客户的统计数量模块数据值 **/
	@RequestMapping(value = "getCustomerOtherTotal", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerOtherTotal(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerService.getCustomerOtherTotal(param);
	}

	/** addCustomer : 新增客户信息 **/
	@RequestMapping(value = "addCustomer", method = RequestMethod.POST)
	public ResponseBodyVO addCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("customer_name,customer_type", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerService.addCustomer(param);
	}

	/** importBatchUser : 批量导入 **/
	@RequestMapping(value = "importBatchCustomer", method = RequestMethod.POST)
	public ResponseBodyVO importBatchUser(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("import_file,import_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerService.importBatchCustomer(param);
	}

	/** updateCustomer : 修改客户信息 **/
	@RequestMapping(value = "updateCustomer", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerService.updateCustomer(param);
	}

	/** delCustomer : 删除客户信息 **/
	@RequestMapping(value = "delCustomer", method = RequestMethod.POST)
	public ResponseBodyVO delCustomer(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerService.delCustomer(param);
	}

}
