package com.cbox.business.user.leavemessage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: LeaveMessageMapper
 * @Function: 用户app咨询留言
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface LeaveMessageMapper {

	/** listLeaveMessage : 获取用户app咨询留言列表数据 **/
	public List<Map<String, Object>> listLeaveMessage(Map<String, Object> param);

	/** getLeaveMessage : 获取指定id的用户app咨询留言数据 **/
	public Map<String, Object> getLeaveMessage(Map<String, Object> param);


}
