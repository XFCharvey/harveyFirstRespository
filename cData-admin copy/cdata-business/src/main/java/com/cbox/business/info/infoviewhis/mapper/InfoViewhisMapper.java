package com.cbox.business.info.infoviewhis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: InfoViewhisMapper
 * @Function: 浏览历史
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface InfoViewhisMapper {

	/** listInfoViewhis : 获取浏览历史列表数据 **/
	public List<Map<String, Object>> listInfoViewhis(Map<String, Object> param);

	/** getInfoViewhis : 获取指定id的浏览历史数据 **/
	public Map<String, Object> getInfoViewhis(Map<String, Object> param);

    public Map<String, Object> getUserInfoViewhis(Map<String, Object> param);

}
