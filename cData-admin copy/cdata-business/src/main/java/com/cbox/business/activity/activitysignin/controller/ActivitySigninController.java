package com.cbox.business.activity.activitysignin.controller;

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
import com.cbox.business.activity.activitysignin.service.ActivitySigninService;


/**
 * @ClassName: ActivitySigninController
 * @Function: 活动签到
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/activity/activitysignin")
public class ActivitySigninController extends BaseController {

	@Autowired
	private ActivitySigninService activitySigninService;

	
	/** listActivitySignin : 获取活动签到列表数据 **/
	@RequestMapping(value = "listActivitySignin", method = RequestMethod.POST)
	public TableDataInfo listActivitySignin(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= activitySigninService.listActivitySignin(param);

		return getDataTable(list);
	}

	/** getActivitySignin : 获取指定id的活动签到数据 **/
	@RequestMapping(value = "getActivitySignin", method = RequestMethod.POST)
	public ResponseBodyVO getActivitySignin(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activitySigninService.getActivitySignin(param);
	}

	/** addActivitySignin : 新增活动签到 **/
	@RequestMapping(value = "addActivitySignin", method = RequestMethod.POST)
	public ResponseBodyVO addActivitySignin(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("activity_id,signin_phone,signing_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activitySigninService.addActivitySignin(param);
	}

	/** updateActivitySignin : 修改活动签到 **/
	@RequestMapping(value = "updateActivitySignin", method = RequestMethod.POST)
	public ResponseBodyVO updateActivitySignin(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activitySigninService.updateActivitySignin(param);
	}

	/** delActivitySignin : 删除活动签到 **/
	@RequestMapping(value = "delActivitySignin", method = RequestMethod.POST)
	public ResponseBodyVO delActivitySignin(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activitySigninService.delActivitySignin(param);
	}


}
