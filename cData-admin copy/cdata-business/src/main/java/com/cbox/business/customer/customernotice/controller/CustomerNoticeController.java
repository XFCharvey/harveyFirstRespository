package com.cbox.business.customer.customernotice.controller;

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
import com.cbox.business.customer.customernotice.service.CustomerNoticeService;


/**
 * @ClassName: MessageNoticeController
 * @Function: 消息通知
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/customer/customernotice")
public class CustomerNoticeController extends BaseController {

	@Autowired
    private CustomerNoticeService customerNoticeService;

	
    /** listCustomerNotice : 获取消息通知列表数据 **/
    @RequestMapping(value = "listCustomerNotice", method = RequestMethod.POST)
    public TableDataInfo listCustomerNotice(@RequestBody Map<String, Object> param) {
        startPage();
        List<Map<String, Object>> list = customerNoticeService.listCustomerNotice(param);

        return getDataTable(list);
	}

    /** listUserCustomerNotice : 列出本人作为置业顾问，所关联的客户尚未过期的相关提醒**/
    @RequestMapping(value = "listUserCustomerNotice", method = RequestMethod.POST)
    public TableDataInfo listUserCustomerNotice(@RequestBody Map<String, Object> param) {
        startPage();
        List<Map<String, Object>> listResult = customerNoticeService.listUserCustomerNotice(param);

        return getDataTable(listResult);
    }


    /** getCustomerNotice : 获取指定id的消息通知数据 **/
    @RequestMapping(value = "getCustomerNotice", method = RequestMethod.POST)
    public ResponseBodyVO getCustomerNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

        return customerNoticeService.getCustomerNotice(param);
	}

    /** addCustomerNotice : 新增消息通知 **/
    @RequestMapping(value = "addCustomerNotice", method = RequestMethod.POST)
    public ResponseBodyVO addCustomerNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("notice_type,notice_content", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

        return customerNoticeService.addCustomerNotice(param);
	}



    /** updateCustomerNotice : 修改消息通知 **/
    @RequestMapping(value = "updateCustomerNotice", method = RequestMethod.POST)
    public ResponseBodyVO updateCustomerNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

        return customerNoticeService.updateCustomerNotice(param);
	}

    /** allDoneNotice : 一键已读消息通知 **/
    @RequestMapping(value = "allDoneNotice", method = RequestMethod.POST)
    public ResponseBodyVO allDoneNotice(@RequestBody Map<String, Object> param) {

        return customerNoticeService.allDoneNotice(param);
    }

    /** delCustomerNotice : 删除消息通知 **/
    @RequestMapping(value = "delCustomerNotice", method = RequestMethod.POST)
    public ResponseBodyVO delCustomerNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

        return customerNoticeService.delCustomerNotice(param);
	}


}
