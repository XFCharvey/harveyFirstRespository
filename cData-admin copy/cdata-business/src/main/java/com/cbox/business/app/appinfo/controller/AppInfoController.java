package com.cbox.business.app.appinfo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.business.app.appinfo.service.AppInfoService;


/**
 * @ClassName: AppInfoController
 * @Function: app登录信息
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/appinfo")
public class AppInfoController extends BaseController {

	@Autowired
	private AppInfoService appInfoService;

	/** getAppInfo : 获取指定id的app登录信息数据 **/
	@RequestMapping(value = "getAppInfo", method = RequestMethod.POST)
	public ResponseBodyVO getAppInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return appInfoService.getAppInfo(param);
	}

	/** addAppInfo : 新增app登录信息 **/
	@RequestMapping(value = "addAppInfo", method = RequestMethod.POST)
	public ResponseBodyVO addAppInfo(@RequestBody Map<String, Object> param) {


		return appInfoService.addAppInfo(param);
	}

	/** updateAppInfo : 修改app登录信息 **/
	@RequestMapping(value = "updateAppInfo", method = RequestMethod.POST)
	public ResponseBodyVO updateAppInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return appInfoService.updateAppInfo(param);
	}

	/** delAppInfo : 删除app登录信息 **/
	@RequestMapping(value = "delAppInfo", method = RequestMethod.POST)
	public ResponseBodyVO delAppInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return appInfoService.delAppInfo(param);
	}


}
