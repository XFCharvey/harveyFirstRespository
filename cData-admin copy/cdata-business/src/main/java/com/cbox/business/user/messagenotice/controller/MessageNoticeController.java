package com.cbox.business.user.messagenotice.controller;

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
import com.cbox.business.user.messagenotice.service.MessageNoticeService;


/**
 * @ClassName: MessageNoticeController
 * @Function: 消息通知
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/user/messagenotice")
public class MessageNoticeController extends BaseController {

	@Autowired
	private MessageNoticeService messageNoticeService;

	
	/** listMessageNotice : 获取消息通知列表数据 **/
	@RequestMapping(value = "listMessageNotice", method = RequestMethod.POST)
    public TableDataInfo listMessageNotice(@RequestBody Map<String, Object> param) {
        startPage();
        List<Map<String, Object>> list = messageNoticeService.listMessageNotice(param);

        return getDataTable(list);
	}


	/** getMessageNotice : 获取指定id的消息通知数据 **/
	@RequestMapping(value = "getMessageNotice", method = RequestMethod.POST)
	public ResponseBodyVO getMessageNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return messageNoticeService.getMessageNotice(param);
	}

	/** addMessageNotice : 新增消息通知 **/
	@RequestMapping(value = "addMessageNotice", method = RequestMethod.POST)
	public ResponseBodyVO addMessageNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("notice_type,notice_content,notice_person,notice_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return messageNoticeService.addMessageNotice(param);
	}



	/** updateMessageNotice : 修改消息通知 **/
	@RequestMapping(value = "updateMessageNotice", method = RequestMethod.POST)
	public ResponseBodyVO updateMessageNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return messageNoticeService.updateMessageNotice(param);
	}

    /** allDoneNotice : 一键已读消息通知 **/
    @RequestMapping(value = "allDoneNotice", method = RequestMethod.POST)
    public ResponseBodyVO allDoneNotice(@RequestBody Map<String, Object> param) {

        return messageNoticeService.allDoneNotice(param);
    }

	/** delMessageNotice : 删除消息通知 **/
	@RequestMapping(value = "delMessageNotice", method = RequestMethod.POST)
	public ResponseBodyVO delMessageNotice(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return messageNoticeService.delMessageNotice(param);
	}


}
