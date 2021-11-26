package com.cbox.business.activity.activitysignin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ActivitySigninMapper
 * @Function: 活动签到
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ActivitySigninMapper {

	/** listActivitySignin : 获取活动签到列表数据 **/
	public List<Map<String, Object>> listActivitySignin(Map<String, Object> param);

	/** getActivitySignin : 获取指定id的活动签到数据 **/
	public Map<String, Object> getActivitySignin(Map<String, Object> param);


}
