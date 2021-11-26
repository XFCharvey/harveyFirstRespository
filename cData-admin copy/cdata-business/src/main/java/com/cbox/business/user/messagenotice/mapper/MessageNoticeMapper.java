package com.cbox.business.user.messagenotice.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MessageNoticeMapper
 * @Function: 消息通知
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface MessageNoticeMapper {

	/** listMessageNotice : 获取消息通知列表数据 **/
	public List<Map<String, Object>> listMessageNotice(Map<String, Object> param);

	/** getMessageNotice : 获取指定id的消息通知数据 **/
	public Map<String, Object> getMessageNotice(Map<String, Object> param);

    public int delNotice(Map<String, Object> param);

    public int allDoneNotice(Map<String, Object> param);

    public List<Map<String, Object>> listOverdueMessageNotice(Map<String, Object> param);

}
