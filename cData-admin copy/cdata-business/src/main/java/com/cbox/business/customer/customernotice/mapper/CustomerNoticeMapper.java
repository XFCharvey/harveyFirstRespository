package com.cbox.business.customer.customernotice.mapper;

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
public interface CustomerNoticeMapper {

    /** listCustomerNotice : 获取消息通知列表数据 **/
    public List<Map<String, Object>> listCustomerNotice(Map<String, Object> param);

    /** getCustomerNotice : 获取指定id的消息通知数据 **/
    public Map<String, Object> getCustomerNotice(Map<String, Object> param);

    public int delNotice(Map<String, Object> param);

    public int allDoneNotice(Map<String, Object> param);

    public List<Map<String, Object>> listOverdueCustomerNotice(Map<String, Object> param);

}
