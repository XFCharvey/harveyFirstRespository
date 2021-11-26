package com.cbox.business.app.apploginlog.controller;

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
import com.cbox.business.app.apploginlog.service.AppLogService;


/**
 * @ClassName: AppLoginLogController
 * @Function: app登录日志
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/apploginlog")
public class AppLogController extends BaseController {

	@Autowired
	private AppLogService appLogService;

	
	/** listAppLoginLog : 获取app登录日志列表数据 **/
	@RequestMapping(value = "listAppLoginLog", method = RequestMethod.POST)
	public TableDataInfo listAppLoginLog(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= appLogService.listAppLoginLog(param);

		return getDataTable(list);
	}

	/** getAppLoginLog : 获取指定id的app登录日志数据 **/
	@RequestMapping(value = "getAppLoginLog", method = RequestMethod.POST)
	public ResponseBodyVO getAppLoginLog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return appLogService.getAppLoginLog(param);
	}

	/** addAppLoginLog : 新增app登录日志 **/
	@RequestMapping(value = "addAppLoginLog", method = RequestMethod.POST)
	public ResponseBodyVO addAppLoginLog(@RequestBody Map<String, Object> param) {

		return appLogService.addAppLoginLog(param);
	}

	/** updateAppLoginLog : 修改app登录日志 **/
	@RequestMapping(value = "updateAppLoginLog", method = RequestMethod.POST)
	public ResponseBodyVO updateAppLoginLog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return appLogService.updateAppLoginLog(param);
	}

	/** delAppLoginLog : 删除app登录日志 **/
	@RequestMapping(value = "delAppLoginLog", method = RequestMethod.POST)
	public ResponseBodyVO delAppLoginLog(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return appLogService.delAppLoginLog(param);
	}


}
