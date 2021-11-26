package com.cbox.business.user.usercustomer.service;

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
import com.cbox.business.user.usercustomer.mapper.UserCustomerMapper;


/**
 * @ClassName: UserCustomerService
 * @Function: 用户的客户
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class UserCustomerService extends BaseService {

    @Autowired
    private UserCustomerMapper userCustomerMapper;
    
	/** listUserCustomer : 获取用户的客户列表数据 **/
	public List<Map<String, Object>> listUserCustomer(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = userCustomerMapper.listUserCustomer(param);

		return list;
	}

	/** getUserCustomer : 获取指定id的用户的客户数据 **/
	public ResponseBodyVO getUserCustomer(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = userCustomerMapper.getUserCustomer(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addUserCustomer : 新增用户的客户 **/
	public ResponseBodyVO addUserCustomer(Map<String, Object> param) {

		// Table:d_user_customer
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("follow_person", param.get("follow_person"));
		mapParam.put("customer_id", param.get("customer_id"));
		int count = this.save( "d_user_customer", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增用户的客户失败");
	}

	/** updateUserCustomer : 修改用户的客户 **/
	public ResponseBodyVO updateUserCustomer(Map<String, Object> param) {

		// Table:d_user_customer
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("follow_person", param.get("follow_person"));
		mapParam.put("customer_id", param.get("customer_id"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_user_customer", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改用户的客户失败"); 
	}

	/** delUserCustomer : 删除用户的客户 **/
	public ResponseBodyVO delUserCustomer(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_user_customer
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_user_customer",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除用户的客户失败");
	}


}
