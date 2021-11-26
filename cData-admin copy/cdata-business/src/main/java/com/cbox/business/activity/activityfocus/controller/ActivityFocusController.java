package com.cbox.business.activity.activityfocus.controller;

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
import com.cbox.business.activity.activityfocus.service.ActivityFocusService;


/**
 * @ClassName: ActivityFocusController
 * @Function: 活动关注
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/activity/activityfocus")
public class ActivityFocusController extends BaseController {

	@Autowired
	private ActivityFocusService activityFocusService;

	
	/** listActivityFocus : 获取活动关注列表数据 **/
	@RequestMapping(value = "listActivityFocus", method = RequestMethod.POST)
	public TableDataInfo listActivityFocus(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= activityFocusService.listActivityFocus(param);

		return getDataTable(list);
	}

	/** getActivityFocus : 获取指定id的活动关注数据 **/
	@RequestMapping(value = "getActivityFocus", method = RequestMethod.POST)
	public ResponseBodyVO getActivityFocus(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activityFocusService.getActivityFocus(param);
	}

	/** addActivityFocus : 新增活动关注 **/
	@RequestMapping(value = "addActivityFocus", method = RequestMethod.POST)
	public ResponseBodyVO addActivityFocus(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("activity_id,focus_person,focus_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activityFocusService.addActivityFocus(param);
	}

	/** delActivityFocus : 删除活动关注 **/
	@RequestMapping(value = "delActivityFocus", method = RequestMethod.POST)
	public ResponseBodyVO delActivityFocus(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("activity_id,focus_person", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activityFocusService.delActivityFocus(param);
	}


}
