package com.cbox.business.user.userservehis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserServehisMapper
 * @Function: 员工用户服务历史
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface UserServehisMapper {

	/** listUserServehis : 获取员工用户服务历史列表数据 **/
	public List<Map<String, Object>> listUserServehis(Map<String, Object> param);

	/** getUserServehis : 获取指定id的员工用户服务历史数据 **/
	public Map<String, Object> getUserServehis(Map<String, Object> param);


}
