package com.cbox.business.activity.activityenroll.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ActivityEnrollMapper
 * @Function: 活动报名登记
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ActivityEnrollMapper {

	/** listActivityEnroll : 获取活动报名登记列表数据 **/
	public List<Map<String, Object>> listActivityEnroll(Map<String, Object> param);

    /** getActivityEnroll : 获取指定的活动报名登记数据 **/
	public Map<String, Object> getActivityEnroll(Map<String, Object> param);

    public Map<String, Object> getActivityEnrollTotal(Map<String, Object> param);

}
