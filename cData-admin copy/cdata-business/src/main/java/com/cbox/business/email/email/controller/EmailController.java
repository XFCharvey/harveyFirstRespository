package com.cbox.business.email.email.controller;

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
import com.cbox.business.email.email.service.EmailService;


/**
 * @ClassName: EmailController
 * @Function: 邮件表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/email")
public class EmailController extends BaseController {

	@Autowired
	private EmailService emailService;

	/** listEmail : 获取邮件表列表数据 **/
	@RequestMapping(value = "listEmail", method = RequestMethod.POST)
	public TableDataInfo listEmail(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= emailService.listEmail(param);

		return getDataTable(list);
	}

	/** getEmail : 获取指定id的邮件表数据 **/
	@RequestMapping(value = "getEmail", method = RequestMethod.POST)
	public ResponseBodyVO getEmail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailService.getEmail(param);
	}

	/** addEmail : 新增邮件表 **/
	@RequestMapping(value = "addEmail", method = RequestMethod.POST)
	public ResponseBodyVO addEmail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("email_url,email_owner,owner_desc", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailService.addEmail(param);
	}

	/** updateEmail : 修改邮件表 **/
	@RequestMapping(value = "updateEmail", method = RequestMethod.POST)
	public ResponseBodyVO updateEmail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailService.updateEmail(param);
	}

	/** delEmail : 删除邮件表 **/
	@RequestMapping(value = "delEmail", method = RequestMethod.POST)
	public ResponseBodyVO delEmail(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return emailService.delEmail(param);
	}


}
