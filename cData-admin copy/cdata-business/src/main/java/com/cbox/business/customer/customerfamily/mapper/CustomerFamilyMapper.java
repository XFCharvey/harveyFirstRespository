package com.cbox.business.customer.customerfamily.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerFamilyMapper
 * @Function: 客户家庭成员
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerFamilyMapper {

	/** listCustomerFamily : 获取客户家庭成员列表数据 **/
	public List<Map<String, Object>> listCustomerFamily(Map<String, Object> param);

	/** getCustomerFamily : 获取指定id的客户家庭成员数据 **/
	public Map<String, Object> getCustomerFamily(Map<String, Object> param);


}
