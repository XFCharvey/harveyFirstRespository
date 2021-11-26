package com.cbox.business.customer.customerfamilylive.service;

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
import com.cbox.business.customer.customerfamilylive.mapper.CustomerFamilyliveMapper;


/**
 * @ClassName: CustomerFamilyliveService
 * @Function: 客户家庭居住现状
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerFamilyliveService extends BaseService {

    @Autowired
    private CustomerFamilyliveMapper customerFamilyliveMapper;
    
	/** listCustomerFamilylive : 获取客户家庭居住现状列表数据 **/
	public List<Map<String, Object>> listCustomerFamilylive(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerFamilyliveMapper.listCustomerFamilylive(param);

		return list;
	}

	/** getCustomerFamilylive : 获取指定id的客户家庭居住现状数据 **/
	public ResponseBodyVO getCustomerFamilylive(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = customerFamilyliveMapper.getCustomerFamilylive(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCustomerFamilylive : 新增客户家庭居住现状 **/
	public ResponseBodyVO addCustomerFamilylive(Map<String, Object> param) {

		// Table:d_customer_familylive
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("customer_id", param.get("customer_id"));
        mapParam.put("live_status", param.get("live_status"));
        mapParam.put("live_pain", param.get("live_pain"));
        mapParam.put("preferred_brand", param.get("preferred_brand"));
        mapParam.put("customer_evaluate", param.get("customer_evaluate"));
        mapParam.put("actual_delivery_time", param.get("actual_delivery_time"));
        mapParam.put("contract_delivery_time", param.get("contract_delivery_time"));
		int count = this.save( "d_customer_familylive", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增客户家庭居住现状失败");
	}

	/** updateCustomerFamilylive : 修改客户家庭居住现状 **/
	public ResponseBodyVO updateCustomerFamilylive(Map<String, Object> param) {

		// Table:d_customer_familylive
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("customer_id", param.get("customer_id"));
        mapParam.put("live_status", param.get("live_status"));
        mapParam.put("live_pain", param.get("live_pain"));
        mapParam.put("preferred_brand", param.get("preferred_brand"));
        mapParam.put("customer_evaluate", param.get("customer_evaluate"));
        mapParam.put("actual_delivery_time", param.get("actual_delivery_time"));
        mapParam.put("contract_delivery_time", param.get("contract_delivery_time"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_customer_familylive", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改客户家庭居住现状失败"); 
	}

	/** delCustomerFamilylive : 删除客户家庭居住现状 **/
	public ResponseBodyVO delCustomerFamilylive(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_customer_familylive
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_customer_familylive",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除客户家庭居住现状失败");
	}


}
