package com.cbox.business.customer.customertag.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerTagMapper
 * @Function: 客户标签
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerTagMapper {

	/** listCustomerTag : 获取客户标签列表数据 **/
	public List<Map<String, Object>> listCustomerTag(Map<String, Object> param);

	/** getCustomerTag : 获取指定id的客户标签数据 **/
	public Map<String, Object> getCustomerTag(Map<String, Object> param);


}
