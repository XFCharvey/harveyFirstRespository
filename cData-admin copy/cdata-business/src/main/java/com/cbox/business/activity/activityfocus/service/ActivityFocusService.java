package com.cbox.business.activity.activityfocus.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.business.activity.activityfocus.mapper.ActivityFocusMapper;


/**
 * @ClassName: ActivityFocusService
 * @Function: 活动关注
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ActivityFocusService extends BaseService {

    @Autowired
    private ActivityFocusMapper activityFocusMapper;
    
	/** listActivityFocus : 获取活动关注列表数据 **/
	public List<Map<String, Object>> listActivityFocus(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = activityFocusMapper.listActivityFocus(param);

		return list;
	}

	/** getActivityFocus : 获取指定id的活动关注数据 **/
	public ResponseBodyVO getActivityFocus(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = activityFocusMapper.getActivityFocus(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addActivityFocus : 新增活动关注 **/
	public ResponseBodyVO addActivityFocus(Map<String, Object> param) {

		// Table:d_activity_focus
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("activity_id", param.get("activity_id"));
		mapParam.put("focus_person", param.get("focus_person"));
		mapParam.put("focus_time", param.get("focus_time"));
        int count = this.saveNoRec("d_activity_focus", mapParam, false);

		return ServerRspUtil.formRspBodyVO(count, "新增活动关注失败");
	}

	/** delActivityFocus : 删除活动关注 **/
	public ResponseBodyVO delActivityFocus(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_activity_focus
		Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("activity_id", param.get("activity_id"));
        mapCondition.put("focus_person", param.get("focus_person"));
        int count = this.deleteEmpty("d_activity_focus", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除活动关注失败");
	}


}
