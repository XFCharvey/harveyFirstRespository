package com.cbox.business.customer.customerfamilylive.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerFamilyliveMapper
 * @Function: 客户家庭居住现状
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerFamilyliveMapper {

	/** listCustomerFamilylive : 获取客户家庭居住现状列表数据 **/
	public List<Map<String, Object>> listCustomerFamilylive(Map<String, Object> param);

	/** getCustomerFamilylive : 获取指定id的客户家庭居住现状数据 **/
	public Map<String, Object> getCustomerFamilylive(Map<String, Object> param);


}
