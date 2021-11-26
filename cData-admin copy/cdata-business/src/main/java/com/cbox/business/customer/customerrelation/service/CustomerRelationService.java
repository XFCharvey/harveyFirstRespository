package com.cbox.business.customer.customerrelation.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.annotation.DataScope;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.customer.customerrelation.mapper.CustomerRelationMapper;

/**
 * @ClassName: CustomerRelationService
 * @Function: 客户关联人
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerRelationService extends BaseService {

	@Autowired
	private CustomerRelationMapper customerRelationMapper;

	/** listCustomerRelation : 获取客户关联人列表数据 **/
	public List<Map<String, Object>> listCustomerRelation(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerRelationMapper.listCustomerRelation(param);

		return list;
	}

	/** listRelationCusHouses : 查询指定客户的关联人的房间号列表 **/
	public List<Map<String, Object>> listRelationCusHouses(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerRelationMapper.listRelationCusHouses(param);

		return list;
	}

	/** getCustomerRelation : 获取指定id的客户关联人数据 **/
	public ResponseBodyVO getCustomerRelation(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = customerRelationMapper.getCustomerRelation(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCustomerRelation : 新增客户关联人 **/
	public ResponseBodyVO addCustomerRelation(Map<String, Object> param) {

		// Table:d_customer_relation
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("customer_id", param.get("customer_id"));
		mapParam.put("relation_customerId", param.get("relation_customerId"));
		int count = this.save("d_customer_relation", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增客户关联人失败");
	}

	/** addCustomerRelationBatch : 批量新增客户关联人 **/
	public ResponseBodyVO addCustomerRelationBatch(Map<String, Object> param) {

		// Table:d_customer_relation
		Map<String, Object> mapParam = new HashMap<String, Object>();
		String relationCustomerIds = StrUtil.getMapValue(param, "relation_customerIds");
		String[] relationCustomerIdsArray = relationCustomerIds.split(",");
		int count = 0;
		if (relationCustomerIdsArray.length > 0) {
			for (int i = 0; i < relationCustomerIdsArray.length; i++) {
				mapParam = new HashMap<String, Object>();
				mapParam.put("customer_id", param.get("customer_id"));
				mapParam.put("relation_customerId", relationCustomerIdsArray[i]);
				count = this.save("d_customer_relation", mapParam);
			}
		}

		return ServerRspUtil.formRspBodyVO(count, "新增客户关联人失败");
	}

	/** updateCustomerRelation : 修改客户关联人 **/
	public ResponseBodyVO updateCustomerRelation(Map<String, Object> param) {

		// Table:d_customer_relation
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("customer_id", param.get("customer_id"));
		mapParam.put("relation_customerId", param.get("relation_customerId"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_customer_relation", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改客户关联人失败");
	}

	/** delCustomerRelation : 删除客户关联人 **/
	public ResponseBodyVO delCustomerRelation(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_customer_relation
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_customer_relation", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除客户关联人失败");
	}

}
