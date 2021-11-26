package com.cbox.business.project.argwbind.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ArgwBindMapper
 * @Function: 阿融官微绑定
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface ArgwBindMapper {

	/** listArgwBind : 获取阿融官微绑定列表数据 **/
	public List<Map<String, Object>> listArgwBind(Map<String, Object> param);

	/** getArgwBind : 获取指定id的阿融官微绑定数据 **/
	public Map<String, Object> getArgwBind(Map<String, Object> param);


}
