package com.cbox.business.app.appinfo.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: AppInfoMapper
 * @Function: app登录信息
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface AppInfoMapper {

	/** getAppInfo : 获取指定id的app登录信息数据 **/
	public Map<String, Object> getAppInfo(Map<String, Object> param);


}
