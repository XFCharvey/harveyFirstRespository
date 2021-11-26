package com.cbox.business.activity.activityfocus.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ActivityFocusMapper
 * @Function: 活动关注
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ActivityFocusMapper {

	/** listActivityFocus : 获取活动关注列表数据 **/
	public List<Map<String, Object>> listActivityFocus(Map<String, Object> param);

	/** getActivityFocus : 获取指定id的活动关注数据 **/
	public Map<String, Object> getActivityFocus(Map<String, Object> param);


}
