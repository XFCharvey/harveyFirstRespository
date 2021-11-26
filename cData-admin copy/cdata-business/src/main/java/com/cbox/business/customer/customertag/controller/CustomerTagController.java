package com.cbox.business.customer.customertag.controller;

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
import com.cbox.business.customer.customertag.service.CustomerTagService;


/**
 * @ClassName: CustomerTagController
 * @Function: 客户标签
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customertag")
public class CustomerTagController extends BaseController {

	@Autowired
	private CustomerTagService customerTagService;

	
	/** listCustomerTag : 获取客户标签列表数据 **/
	@RequestMapping(value = "listCustomerTag", method = RequestMethod.POST)
	public TableDataInfo listCustomerTag(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= customerTagService.listCustomerTag(param);

		return getDataTable(list);
	}

	/** getCustomerTag : 获取指定id的客户标签数据 **/
	@RequestMapping(value = "getCustomerTag", method = RequestMethod.POST)
	public ResponseBodyVO getCustomerTag(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerTagService.getCustomerTag(param);
	}

	/** addCustomerTag : 新增客户标签 **/
	@RequestMapping(value = "addCustomerTag", method = RequestMethod.POST)
	public ResponseBodyVO addCustomerTag(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("tag_name", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerTagService.addCustomerTag(param);
	}

	/** updateCustomerTag : 修改客户标签 **/
	@RequestMapping(value = "updateCustomerTag", method = RequestMethod.POST)
	public ResponseBodyVO updateCustomerTag(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerTagService.updateCustomerTag(param);
	}

	/** delCustomerTag : 删除客户标签 **/
	@RequestMapping(value = "delCustomerTag", method = RequestMethod.POST)
	public ResponseBodyVO delCustomerTag(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return customerTagService.delCustomerTag(param);
	}


}
