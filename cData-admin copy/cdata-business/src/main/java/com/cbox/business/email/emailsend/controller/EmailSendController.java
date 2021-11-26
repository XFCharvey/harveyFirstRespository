package com.cbox.business.email.emailsend.controller;

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
import com.cbox.business.email.emailsend.service.EmailSendService;


/**
 * @ClassName: EmailSendController
 * @Function: 邮件发送详情表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/email/emailsend")
public class EmailSendController extends BaseController {

	@Autowired
	private EmailSendService emailSendService;

	
	/** listEmailSend : 获取邮件发送详情表列表数据 **/
	@RequestMapping(value = "listEmailSend", method = RequestMethod.POST)
	public TableDataInfo listEmailSend(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= emailSendService.listEmailSend(param);

		return getDataTable(list);
	}

    // /** sendEmail : 发送邮件 **/
    // @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    // public ResponseBodyVO sendEmail(@RequestBody Map<String, Object> param) {
    // // 校验必填参数
    // String checkResult = this.validInput("email_id,email_content,email_title", param);
    // if (!VALID_SUCC.equals(checkResult)) {
    // return ServerRspUtil.error(checkResult);
    // }
    // return emailSendService.sendEmail(param);
    // }

    @RequestMapping(value = "countMailBySend", method = RequestMethod.POST)
    public TableDataInfo countMailBySend(Map<String, Object> param) {
        startPage();
        List<Map<String, Object>> list = emailSendService.countMailBySend(param);

        return getDataTable(list);
    }

    @RequestMapping(value = "countMailByResv", method = RequestMethod.POST)
    public TableDataInfo countMailByResv(Map<String, Object> param) {
        startPage();
        List<Map<String, Object>> list = emailSendService.countMailByResv(param);

        return getDataTable(list);
    }

	/** getEmailSend : 获取指定id的邮件发送详情表数据 **/
	@RequestMapping(value = "getEmailSend", method = RequestMethod.POST)
	public ResponseBodyVO getEmailSend(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailSendService.getEmailSend(param);
	}

	/** addEmailSend : 新增邮件发送详情表 **/
	@RequestMapping(value = "addEmailSend", method = RequestMethod.POST)
	public ResponseBodyVO addEmailSend(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("email_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailSendService.addEmailSend(param);
	}

	/** updateEmailSend : 修改邮件发送详情表 **/
	@RequestMapping(value = "updateEmailSend", method = RequestMethod.POST)
	public ResponseBodyVO updateEmailSend(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailSendService.updateEmailSend(param);
	}

	/** delEmailSend : 删除邮件发送详情表 **/
	@RequestMapping(value = "delEmailSend", method = RequestMethod.POST)
	public ResponseBodyVO delEmailSend(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailSendService.delEmailSend(param);
	}


}
