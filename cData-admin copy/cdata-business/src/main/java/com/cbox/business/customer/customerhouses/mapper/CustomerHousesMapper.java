package com.cbox.business.customer.customerhouses.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerHousesMapper
 * @Function: 房间客户关联
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerHousesMapper {

	/** listCustomerHouses : 获取房间客户关联列表数据 **/
	public List<Map<String, Object>> listCustomerHouses(Map<String, Object> param);

	/** getCustomerHouses : 获取指定id的房间客户关联数据 **/
	public Map<String, Object> getCustomerHouses(Map<String, Object> param);


}
