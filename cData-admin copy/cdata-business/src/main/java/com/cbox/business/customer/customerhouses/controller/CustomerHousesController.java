package com.cbox.business.customer.customerhouses.controller;

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
import com.cbox.business.customer.customerhouses.service.CustomerHousesService;


/**
 * @ClassName: CustomerHousesController
 * @Function: 房间客户关联
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customerhouses")
public class CustomerHousesController extends BaseController {

	@Autowired
	private CustomerHousesService customerHousesService;

	
	/** listCustomerHouses : 获取房间客户关联列表数据 **/
	@RequestMapping(value = "listCustomerHouses", method = RequestMethod.POST)
	public TableDataInfo listCustomerHouses(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= customerHousesService.listCustomerHouses(param);

		return getDataTable(list);
	}

	/** getCustomerHouses : 获取指定id的房间客户关联数据 **/
	@RequestMapping(value = "getCustomerHouses", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerHousesService.getCustomerHouses(param);
	}

	/** addCustomerHouses : 新增房间客户关联 **/
	@RequestMapping(value = "addCustomerHouses", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("house_id,customer_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerHousesService.addCustomerHouses(param);
	}

	/** updateCustomerHouses : 修改房间客户关联 **/
	@RequestMapping(value = "updateCustomerHouses", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomerHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerHousesService.updateCustomerHouses(param);
	}

	/** delCustomerHouses : 删除房间客户关联 **/
	@RequestMapping(value = "delCustomerHouses", method = RequestMethod.POST)
	public ResponseBodyVO delCustomerHouses(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerHousesService.delCustomerHouses(param);
	}


}
