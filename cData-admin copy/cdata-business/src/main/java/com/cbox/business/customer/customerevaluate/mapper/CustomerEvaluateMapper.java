package com.cbox.business.customer.customerevaluate.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerEvaluateMapper
 * @Function: 客户评价反馈
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerEvaluateMapper {

	/** listCustomerEvaluate : 获取客户评价反馈列表数据 **/
	public List<Map<String, Object>> listCustomerEvaluate(Map<String, Object> param);

	/** getCustomerEvaluate : 获取指定id的客户评价反馈数据 **/
	public Map<String, Object> getCustomerEvaluate(Map<String, Object> param);


}
