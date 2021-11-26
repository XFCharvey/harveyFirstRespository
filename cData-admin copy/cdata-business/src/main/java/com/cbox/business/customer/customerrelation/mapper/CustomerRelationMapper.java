package com.cbox.business.customer.customerrelation.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: CustomerRelationMapper
 * @Function: 客户关联人
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface CustomerRelationMapper {

	/** listCustomerRelation : 获取客户关联人列表数据 **/
	public List<Map<String, Object>> listCustomerRelation(Map<String, Object> param);

	/** getCustomerRelation : 获取指定id的客户关联人数据 **/
	public Map<String, Object> getCustomerRelation(Map<String, Object> param);

	/** listRelationCusHouses : 查询指定客户的关联人的房间号列表 **/
	public List<Map<String, Object>> listRelationCusHouses(Map<String, Object> param);
}
