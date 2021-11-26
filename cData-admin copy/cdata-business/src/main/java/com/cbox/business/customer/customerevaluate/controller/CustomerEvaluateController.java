package com.cbox.business.customer.customerevaluate.controller;

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
import com.cbox.business.customer.customerevaluate.service.CustomerEvaluateService;


/**
 * @ClassName: CustomerEvaluateController
 * @Function: 客户评价反馈
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customerevaluate")
public class CustomerEvaluateController extends BaseController {

	@Autowired
	private CustomerEvaluateService customerEvaluateService;

	
	/** listCustomerEvaluate : 获取客户评价反馈列表数据 **/
	@RequestMapping(value = "listCustomerEvaluate", method = RequestMethod.POST)
	public TableDataInfo listCustomerEvaluate(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= customerEvaluateService.listCustomerEvaluate(param);

		return getDataTable(list);
	}

	/** getCustomerEvaluate : 获取指定id的客户评价反馈数据 **/
	@RequestMapping(value = "getCustomerEvaluate", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerEvaluate(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerEvaluateService.getCustomerEvaluate(param);
	}

	/** addCustomerEvaluate : 新增客户评价反馈 **/
	@RequestMapping(value = "addCustomerEvaluate", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerEvaluate(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("customer_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerEvaluateService.addCustomerEvaluate(param);
	}

	/** updateCustomerEvaluate : 修改客户评价反馈 **/
	@RequestMapping(value = "updateCustomerEvaluate", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomerEvaluate(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerEvaluateService.updateCustomerEvaluate(param);
	}

	/** delCustomerEvaluate : 删除客户评价反馈 **/
	@RequestMapping(value = "delCustomerEvaluate", method = RequestMethod.POST)
	public ResponseBodyVO delCustomerEvaluate(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerEvaluateService.delCustomerEvaluate(param);
	}


}
