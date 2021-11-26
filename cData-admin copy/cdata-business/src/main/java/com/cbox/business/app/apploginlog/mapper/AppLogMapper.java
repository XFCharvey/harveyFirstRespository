package com.cbox.business.app.apploginlog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: AppLoginLogMapper
 * @Function: app登录日志
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface AppLogMapper {

	/** listAppLoginLog : 获取app登录日志列表数据 **/
	public List<Map<String, Object>> listAppLoginLog(Map<String, Object> param);

	/** getAppLoginLog : 获取指定id的app登录日志数据 **/
	public Map<String, Object> getAppLoginLog(Map<String, Object> param);


}
