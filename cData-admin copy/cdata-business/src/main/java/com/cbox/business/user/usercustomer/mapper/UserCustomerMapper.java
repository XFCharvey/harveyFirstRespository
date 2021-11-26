package com.cbox.business.user.usercustomer.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserCustomerMapper
 * @Function: 用户的客户
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface UserCustomerMapper {

	/** listUserCustomer : 获取用户的客户列表数据 **/
	public List<Map<String, Object>> listUserCustomer(Map<String, Object> param);

	/** getUserCustomer : 获取指定id的用户的客户数据 **/
	public Map<String, Object> getUserCustomer(Map<String, Object> param);


}
