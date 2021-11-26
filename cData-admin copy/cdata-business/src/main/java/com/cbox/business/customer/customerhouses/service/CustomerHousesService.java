package com.cbox.business.customer.customerhouses.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.business.customer.customerhouses.mapper.CustomerHousesMapper;


/**
 * @ClassName: CustomerHousesService
 * @Function: 房间客户关联
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerHousesService extends BaseService {

    @Autowired
    private CustomerHousesMapper customerHousesMapper;
    
	/** listCustomerHouses : 获取房间客户关联列表数据 **/
	public List<Map<String, Object>> listCustomerHouses(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerHousesMapper.listCustomerHouses(param);

		return list;
	}

	/** getCustomerHouses : 获取指定id的房间客户关联数据 **/
	public ResponseBodyVO getCustomerHouses(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = customerHousesMapper.getCustomerHouses(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCustomerHouses : 新增房间客户关联 **/
	public ResponseBodyVO addCustomerHouses(Map<String, Object> param) {

		// Table:d_customer_houses
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("house_id", param.get("house_id"));
		mapParam.put("customer_id", param.get("customer_id"));
		int count = this.save( "d_customer_houses", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增房间客户关联失败");
	}

	/** updateCustomerHouses : 修改房间客户关联 **/
	public ResponseBodyVO updateCustomerHouses(Map<String, Object> param) {

		// Table:d_customer_houses
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("house_id", param.get("house_id"));
		mapParam.put("customer_id", param.get("customer_id"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_customer_houses", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改房间客户关联失败"); 
	}

	/** delCustomerHouses : 删除房间客户关联 **/
	public ResponseBodyVO delCustomerHouses(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_customer_houses
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_customer_houses",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除房间客户关联失败");
	}


}
