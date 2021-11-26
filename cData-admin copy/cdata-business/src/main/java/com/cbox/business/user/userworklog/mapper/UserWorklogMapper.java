package com.cbox.business.user.userworklog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserWorklogMapper
 * @Function: 员工工作日志
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface UserWorklogMapper {

	/** listUserWorklog : 获取员工工作日志列表数据 **/
	public List<Map<String, Object>> listUserWorklog(Map<String, Object> param);

	/** getUserWorklog : 获取指定id的员工工作日志数据 **/
	public Map<String, Object> getUserWorklog(Map<String, Object> param);
	
	/** listUserWorklogByDeptId : 获取指定年月当前登陆用户团队及下级团队员工工作日志量列表数据 **/
	public List<Map<String, Object>> listUserWorklogByDeptId(Map<String, Object> param);
	
	/** listUserWorklogCountOfDay : 获取指定年月当前登陆用户团队及下级团队员工工作日志每日数量列表数据 **/
	public List<Map<String, Object>> listUserWorklogCountOfDay(Map<String, Object> param);


}
