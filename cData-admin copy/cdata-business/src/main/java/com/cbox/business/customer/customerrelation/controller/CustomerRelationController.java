package com.cbox.business.customer.customerrelation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;

import com.cbox.business.customer.customerrelation.service.CustomerRelationService;

/**
 * @ClassName: CustomerRelationController
 * @Function: 客户关联人
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customerrelation")
public class CustomerRelationController extends BaseController {

	@Autowired
	private CustomerRelationService customerRelationService;

	/** listCustomerRelation : 获取客户关联人列表数据 **/
	@RequestMapping(value = "listCustomerRelation", method = RequestMethod.POST)
	public TableDataInfo listCustomerRelation(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = customerRelationService.listCustomerRelation(param);

		return getDataTable(list);
	}

	/** listRelationCusHouses : 查询指定客户的关联人的房间号列表 **/
	@RequestMapping(value = "listRelationCusHouses", method = RequestMethod.POST)
	public TableDataInfo listRelationCusHouses(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = customerRelationService.listRelationCusHouses(param);

		return getDataTable(list);
	}

	/** getCustomerRelation : 获取指定id的客户关联人数据 **/
	@RequestMapping(value = "getCustomerRelation", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerRelation(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerRelationService.getCustomerRelation(param);
	}

	/** addCustomerRelation : 新增客户关联人 **/
	@RequestMapping(value = "addCustomerRelation", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerRelation(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("customer_id,relation_customerId", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerRelationService.addCustomerRelation(param);
	}

	/** addCustomerRelationBatch : 批量新增客户关联人 **/
	@RequestMapping(value = "addCustomerRelationBatch", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerRelationBatch(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("customer_id,relation_customerIds", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerRelationService.addCustomerRelationBatch(param);
	}

	/** updateCustomerRelation : 修改客户关联人 **/
	@RequestMapping(value = "updateCustomerRelation", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomerRelation(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerRelationService.updateCustomerRelation(param);
	}

	/** delCustomerRelation : 删除客户关联人 **/
	@RequestMapping(value = "delCustomerRelation", method = RequestMethod.POST)
	public ResponseBodyVO delCustomerRelation(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerRelationService.delCustomerRelation(param);
	}

}
