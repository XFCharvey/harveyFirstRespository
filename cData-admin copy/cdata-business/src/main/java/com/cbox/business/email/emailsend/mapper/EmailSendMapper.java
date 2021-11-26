package com.cbox.business.email.emailsend.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: EmailSendMapper
 * @Function: 邮件发送详情表
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface EmailSendMapper {

	/** listEmailSend : 获取邮件发送详情表列表数据 **/
	public List<Map<String, Object>> listEmailSend(Map<String, Object> param);

	/** getEmailSend : 获取指定id的邮件发送详情表数据 **/
	public Map<String, Object> getEmailSend(Map<String, Object> param);

    public List<Map<String, Object>> countMailBySend(Map<String, Object> param);

    public List<Map<String, Object>> countMailByResv(Map<String, Object> param);

}
