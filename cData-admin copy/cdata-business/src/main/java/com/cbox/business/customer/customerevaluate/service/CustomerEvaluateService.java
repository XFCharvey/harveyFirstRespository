package com.cbox.business.customer.customerevaluate.service;

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
import com.cbox.business.customer.customerevaluate.mapper.CustomerEvaluateMapper;


/**
 * @ClassName: CustomerEvaluateService
 * @Function: 客户评价反馈
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerEvaluateService extends BaseService {

    @Autowired
    private CustomerEvaluateMapper customerEvaluateMapper;
    
	/** listCustomerEvaluate : 获取客户评价反馈列表数据 **/
	public List<Map<String, Object>> listCustomerEvaluate(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerEvaluateMapper.listCustomerEvaluate(param);

		return list;
	}

	/** getCustomerEvaluate : 获取指定id的客户评价反馈数据 **/
	public ResponseBodyVO getCustomerEvaluate(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = customerEvaluateMapper.getCustomerEvaluate(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCustomerEvaluate : 新增客户评价反馈 **/
	public ResponseBodyVO addCustomerEvaluate(Map<String, Object> param) {

		// Table:d_customer_evaluate
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("customer_id", param.get("customer_id"));
        mapParam.put("product_quality", param.get("product_quality"));
        mapParam.put("product_design", param.get("product_design"));
        mapParam.put("product_aftersale", param.get("product_aftersale"));
        mapParam.put("product_memory", param.get("product_memory"));
		int count = this.save( "d_customer_evaluate", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增客户评价反馈失败");
	}

	/** updateCustomerEvaluate : 修改客户评价反馈 **/
	public ResponseBodyVO updateCustomerEvaluate(Map<String, Object> param) {

		// Table:d_customer_evaluate
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("customer_id", param.get("customer_id"));
        mapParam.put("product_quality", param.get("product_quality"));
        mapParam.put("product_design", param.get("product_design"));
        mapParam.put("product_aftersale", param.get("product_aftersale"));
        mapParam.put("product_memory", param.get("product_memory"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_customer_evaluate", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改客户评价反馈失败"); 
	}

	/** delCustomerEvaluate : 删除客户评价反馈 **/
	public ResponseBodyVO delCustomerEvaluate(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_customer_evaluate
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_customer_evaluate",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除客户评价反馈失败");
	}


}
