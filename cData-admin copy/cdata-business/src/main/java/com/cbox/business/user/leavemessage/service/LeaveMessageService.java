package com.cbox.business.user.leavemessage.service;

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
import com.cbox.business.user.leavemessage.mapper.LeaveMessageMapper;


/**
 * @ClassName: LeaveMessageService
 * @Function: 用户app咨询留言
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class LeaveMessageService extends BaseService {

    @Autowired
    private LeaveMessageMapper leaveMessageMapper;
    
	/** listLeaveMessage : 获取用户app咨询留言列表数据 **/
    public List<Map<String, Object>> listLeaveMessage(Map<String, Object> param, boolean isUser) {
		super.appendUserInfo(param);
        if (isUser) {
            String userName = SecurityUtils.getUsername();
            param.put("rec_person", userName);
        }
		List<Map<String, Object>> list = leaveMessageMapper.listLeaveMessage(param);

		return list;
	}

	/** getLeaveMessage : 获取指定id的用户app咨询留言数据 **/
	public ResponseBodyVO getLeaveMessage(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = leaveMessageMapper.getLeaveMessage(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addLeaveMessage : 新增用户app咨询留言 **/
	public ResponseBodyVO addLeaveMessage(Map<String, Object> param) {

		// Table:d_user_leave_message
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("leave_message", param.get("leave_message"));
        mapParam.put("leave_img", param.get("leave_img"));
		int count = this.save( "d_user_leave_message", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增用户app咨询留言失败");
	}

	/** updateLeaveMessage : 修改用户app咨询留言 **/
	public ResponseBodyVO updateLeaveMessage(Map<String, Object> param) {

		// Table:d_user_leave_message
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("leave_message", param.get("leave_message"));
        mapParam.put("leave_img", param.get("leave_img"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_user_leave_message", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改用户app咨询留言失败"); 
	}

	/** delLeaveMessage : 删除用户app咨询留言 **/
	public ResponseBodyVO delLeaveMessage(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_user_leave_message
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_user_leave_message",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除用户app咨询留言失败");
	}


}
