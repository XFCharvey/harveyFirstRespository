package com.cbox.business.user.userremind.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserRemindMapper
 * @Function: 员工个人提醒
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface UserRemindMapper {

	/** listUserRemind : 获取员工个人提醒列表数据 **/
	public List<Map<String, Object>> listUserRemind(Map<String, Object> param);

	/** getUserRemind : 获取指定id的员工个人提醒数据 **/
	public Map<String, Object> getUserRemind(Map<String, Object> param);


}
