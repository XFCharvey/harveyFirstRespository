package com.cbox.business.customer.customer.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerMapper
 * @Function: 客户信息
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerMapper {

	/** listCustomer : 获取客户信息列表数据 **/
	public List<Map<String, Object>> listCustomer(Map<String, Object> param);
	
	/** listCustomerOfHouse : 获取导出客户信息列表数据 **/
	public List<Map<String, Object>> listCustomerOfHouse(Map<String, Object> param);
	
	/** listCustomerApp : app获取客户信息列表数据 **/
	public List<Map<String, Object>> listCustomerApp(Map<String, Object> param);

	/** getCustomer : 获取指定id的客户信息数据 **/
	public Map<String, Object> getCustomer(Map<String, Object> param);


}
