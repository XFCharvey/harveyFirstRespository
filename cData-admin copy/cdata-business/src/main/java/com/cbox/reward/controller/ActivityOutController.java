package com.cbox.reward.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.activity.activity.service.ActivityService;
import com.google.zxing.WriterException;
import com.cbox.business.activity.activitysignin.service.ActivitySigninService;

/**
 * 活动相关
 *
 */
@RestController
@RequestMapping("/lottery")
public class ActivityOutController extends BaseController {

	@Autowired
	ActivityService activityService;

	@Autowired
	ActivitySigninService ActivitySigninService;

	/** getActivity : 获取指定id的活动数据 **/
	@PostMapping("getActivity")
	public ResponseBodyVO getActivity(@RequestBody Map<String, Object> param) {
		// 校验必填参数
		String checkResult = this.validInput("activity_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		param.put("rec_id", StrUtil.getMapValue(param, "activity_id"));

		return activityService.getActivity(param);
	}

	/**
	 * signActivity : 活动签到
	 * 
	 * @throws WriterException
	 * @throws IOException
	 **/
	@PostMapping("signActivity")
	public ResponseBodyVO signActivity(@RequestBody Map<String, Object> param) throws IOException, WriterException {
		// 校验必填参数
		String checkResult = this.validInput("activity_id,phone_no,check_code", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return ActivitySigninService.addActivitySignin(param);
	}

	/** getCheckCode : 获取短信校验码 **/
	@PostMapping("getCheckCode")
	public ResponseBodyVO getCheckCode(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("phone_no", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		// todo..
		return ServerRspUtil.success();
	}

}