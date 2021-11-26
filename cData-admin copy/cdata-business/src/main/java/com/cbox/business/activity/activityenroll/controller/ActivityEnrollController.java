package com.cbox.business.activity.activityenroll.controller;

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
import com.cbox.business.activity.activityenroll.service.ActivityEnrollService;


/**
 * @ClassName: ActivityEnrollController
 * @Function: 活动报名登记
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/activity/activityenroll")
public class ActivityEnrollController extends BaseController {

	@Autowired
	private ActivityEnrollService activityEnrollService;

	
	/** listActivityEnroll : 获取活动报名登记列表数据 **/
	@RequestMapping(value = "listActivityEnroll", method = RequestMethod.POST)
	public TableDataInfo listActivityEnroll(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= activityEnrollService.listActivityEnroll(param);

		return getDataTable(list);
	}

	/** getActivityEnroll : 获取指定id的活动报名登记数据 **/
	@RequestMapping(value = "getActivityEnroll", method = RequestMethod.POST)
	public ResponseBodyVO getActivityEnroll(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("activity_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activityEnrollService.getActivityEnroll(param);
	}

	/** addActivityEnroll : 新增活动报名登记 **/
	@RequestMapping(value = "addActivityEnroll", method = RequestMethod.POST)
	public ResponseBodyVO addActivityEnroll(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("activity_id,e_name,e_tell,e_dept,e_person", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activityEnrollService.addActivityEnroll(param);
	}

	/** updateActivityEnroll : 修改活动报名登记 **/
	@RequestMapping(value = "updateActivityEnroll", method = RequestMethod.POST)
    public ResponseBodyVO updateActivityEnroll(@RequestBody Map<String, Object> param) {

		return activityEnrollService.updateActivityEnroll(param);
	}

	/** delActivityEnroll : 删除活动报名登记 **/
	@RequestMapping(value = "delActivityEnroll", method = RequestMethod.POST)
	public ResponseBodyVO delActivityEnroll(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return activityEnrollService.delActivityEnroll(param);
	}


}
