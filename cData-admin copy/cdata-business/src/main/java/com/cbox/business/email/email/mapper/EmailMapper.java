package com.cbox.business.email.email.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: EmailMapper
 * @Function: 邮件表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface EmailMapper {

	/** listEmail : 获取邮件表列表数据 **/
	public List<Map<String, Object>> listEmail(Map<String, Object> param);

	/** getEmail : 获取指定id的邮件表数据 **/
	public Map<String, Object> getEmail(Map<String, Object> param);


}
