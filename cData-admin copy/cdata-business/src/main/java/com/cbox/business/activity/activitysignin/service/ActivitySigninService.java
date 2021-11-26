package com.cbox.business.activity.activitysignin.service;

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
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.activity.activitysignin.mapper.ActivitySigninMapper;
import com.google.common.collect.ImmutableMap;

/**
 * @ClassName: ActivitySigninService
 * @Function: 活动签到
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ActivitySigninService extends BaseService {

	@Autowired
	private ActivitySigninMapper activitySigninMapper;

	/** listActivitySignin : 获取活动签到列表数据 **/
	public List<Map<String, Object>> listActivitySignin(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = activitySigninMapper.listActivitySignin(param);

		return list;
	}

	/** getActivitySignin : 获取指定id的活动签到数据 **/
	public ResponseBodyVO getActivitySignin(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = activitySigninMapper.getActivitySignin(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addActivitySignin : 新增活动签到 **/
	public ResponseBodyVO addActivitySignin(Map<String, Object> param) {
		// Table:d_activity_signin
		// 检查此手机号是否签到
		String phone = StrUtil.getMapValue(param, "phone_no");
		boolean flag = checkPhone(phone);

		int count = 0;
		String msg = null;
		if (flag) {
			msg = "您已签到，请勿重复操作";
			count=-1;
		} else {
			String nowTime = DateUtils.getTime();
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("activity_id", param.get("activity_id"));
			mapParam.put("signin_phone", phone);
			mapParam.put("signing_time", nowTime);
			msg = "签到失败";
			count = this.saveNoRec("d_activity_signin", mapParam, false);
		}

		return ServerRspUtil.formRspBodyVO(count, msg);
	}

	public boolean checkPhone(String phone) {
		boolean existFlag = false;
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("signin_phone", phone);
		List<Map<String, Object>> list = activitySigninMapper.listActivitySignin(mapParam);
		if (list.size() > 0) {
			existFlag = true;
		}

		return existFlag;
	}

	/** updateActivitySignin : 修改活动签到 **/
	public ResponseBodyVO updateActivitySignin(Map<String, Object> param) {

		// Table:d_activity_signin
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("activity_id", param.get("activity_id"));
		mapParam.put("signin_phone", param.get("signin_phone"));
		mapParam.put("signing_time", param.get("signing_time"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.updateNoRec(mapCondition, "d_activity_signin", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改活动签到失败");
	}

	/** delActivitySignin : 删除活动签到 **/
	public ResponseBodyVO delActivitySignin(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_activity_signin
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.deleteEmpty("d_activity_signin", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除活动签到失败");
	}

}
