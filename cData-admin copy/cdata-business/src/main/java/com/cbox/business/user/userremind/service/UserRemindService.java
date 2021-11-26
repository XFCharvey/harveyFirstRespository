package com.cbox.business.user.userremind.service;

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
import com.cbox.base.utils.SecurityUtils;
import com.cbox.business.user.userremind.mapper.UserRemindMapper;


/**
 * @ClassName: UserRemindService
 * @Function: 员工个人提醒
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class UserRemindService extends BaseService {

    @Autowired
    private UserRemindMapper userRemindMapper;
    
	/** listUserRemind : 获取员工个人提醒列表数据 **/
	public List<Map<String, Object>> listUserRemind(Map<String, Object> param) {
		super.appendUserInfo(param);
        String userName = SecurityUtils.getUsername();
        param.put("remind_person", userName);
		List<Map<String, Object>> list = userRemindMapper.listUserRemind(param);

		return list;
	}

	/** getUserRemind : 获取指定id的员工个人提醒数据 **/
	public ResponseBodyVO getUserRemind(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = userRemindMapper.getUserRemind(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addUserRemind : 新增员工个人提醒 **/
	public ResponseBodyVO addUserRemind(Map<String, Object> param) {
        String userName = SecurityUtils.getUsername();
		// Table:d_user_remind
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("remind_title", param.get("remind_title"));
		mapParam.put("remind_time", param.get("remind_time"));
		mapParam.put("remind_desc", param.get("remind_desc"));
        mapParam.put("remind_person", userName);
        int count = this.save("d_user_remind", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增员工个人提醒失败");
	}

	/** updateUserRemind : 修改员工个人提醒 **/
	public ResponseBodyVO updateUserRemind(Map<String, Object> param) {

		// Table:d_user_remind
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("remind_title", param.get("remind_title"));
		mapParam.put("remind_time", param.get("remind_time"));
		mapParam.put("remind_desc", param.get("remind_desc"));
        mapParam.put("remind_person", param.get("remind_person"));
        mapParam.put("remind_status", param.get("remind_status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.update(mapCondition, "d_user_remind", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改员工个人提醒失败"); 
	}

	/** delUserRemind : 删除员工个人提醒 **/
	public ResponseBodyVO delUserRemind(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_user_remind
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_user_remind",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除员工个人提醒失败");
	}


}
