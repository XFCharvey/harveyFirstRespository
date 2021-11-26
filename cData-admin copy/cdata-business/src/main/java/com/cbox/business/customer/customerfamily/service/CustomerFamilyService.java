package com.cbox.business.customer.customerfamily.service;

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
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.customer.customerfamily.mapper.CustomerFamilyMapper;
import com.cbox.business.customer.customerhouses.mapper.CustomerHousesMapper;

/**
 * @ClassName: CustomerFamilyService
 * @Function: 客户家庭成员
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerFamilyService extends BaseService {

    @Autowired
    private CustomerFamilyMapper customerFamilyMapper;
    @Autowired
    private CustomerHousesMapper customerHousesMapper;
    
    /** listCustomerFamily : 获取客户家庭成员列表数据 **/
    public List<Map<String, Object>> listCustomerFamily(Map<String, Object> param) {
        super.appendUserInfo(param);

        List<Map<String, Object>> list = customerFamilyMapper.listCustomerFamily(param);

        return list;
    }

    /** getCustomerFamily : 获取指定id的客户家庭成员数据 **/
    public ResponseBodyVO getCustomerFamily(Map<String, Object> param) {
        super.appendUserInfo(param);

        Map<String, Object> mapResult = customerFamilyMapper.getCustomerFamily(param);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
    }

    /** addCustomerFamily : 新增客户家庭成员 **/
    public ResponseBodyVO addCustomerFamily(Map<String, Object> param) {
        int count = 0;
        String familyPhone = StrUtil.getMapValue(param, "family_phone");
        Map<String, Object> mapFamilyCondition = new HashMap<String, Object>();
        mapFamilyCondition.put("family_phone", familyPhone);
        List<Map<String, Object>> listFamilyByPhone = customerFamilyMapper.listCustomerFamily(mapFamilyCondition);
        if (ObjUtil.isNotNull(listFamilyByPhone)) {
            return ServerRspUtil.formRspBodyVO(count, "该成员手机号已存在，请确认是否重复");
        } else {
            // Table:d_customer_family
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("customer_id", param.get("customer_id"));
            mapParam.put("family_name", param.get("family_name"));
            mapParam.put("family_relations", param.get("family_relations"));
            mapParam.put("family_phone", param.get("family_phone"));
            mapParam.put("family_birthday", param.get("family_birthday"));
            mapParam.put("family_hobby", param.get("family_hobby"));
            count += this.save("d_customer_family", mapParam);
            return ServerRspUtil.formRspBodyVO(count, "新增客户家庭成员失败");

        }
    }

    /** updateCustomerFamily : 修改客户家庭成员 **/
    public ResponseBodyVO updateCustomerFamily(Map<String, Object> param) {
        int count = 0;
        // Table:d_customer_family
        Map<String, Object> mapOldCustomerFamilyCondition = new HashMap<String, Object>();
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("customer_id", param.get("customer_id"));
        StrUtil.setMapValue(mapOldCustomerFamilyCondition, "rec_id", param);
        mapParam.put("family_name", param.get("family_name"));
        mapParam.put("family_relations", param.get("family_relations"));
        mapParam.put("family_birthday", param.get("family_birthday"));
        mapParam.put("family_hobby", param.get("family_hobby"));
        
        Map<String, Object> mapOldCustomerFamily = customerFamilyMapper.getCustomerFamily(mapOldCustomerFamilyCondition);
        String oldCustomerFamilyPhone = StrUtil.getMapValue(mapOldCustomerFamily, "family_phone");
        String customerFamilyPhoneParam = StrUtil.getMapValue(param, "family_phone");
        if (oldCustomerFamilyPhone.equals(customerFamilyPhoneParam)) {
            mapParam.put("family_phone", param.get("family_phone"));
        }else {
            Map<String, Object> mapFamilyCondition = new HashMap<String, Object>();
            mapFamilyCondition.put("family_phone", customerFamilyPhoneParam);
            List<Map<String, Object>> listFamilyByPhone = customerFamilyMapper.listCustomerFamily(mapFamilyCondition);
            if (ObjUtil.isNotNull(listFamilyByPhone)) {
                return ServerRspUtil.formRspBodyVO(count, "该成员手机号已存在，请确认是否重复");
            } else {
                mapParam.put("family_phone", param.get("family_phone"));
            }
        }
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        count = this.update(mapCondition, "d_customer_family", mapParam);

        return ServerRspUtil.formRspBodyVO(count, "修改客户家庭成员失败");
    }

    /** delCustomerFamily : 删除客户家庭成员 **/
    public ResponseBodyVO delCustomerFamily(Map<String, Object> param) {

        // TODO : 删除前的逻辑判断

        // Table:d_customer_family
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.delete("d_customer_family", mapCondition);

        return ServerRspUtil.formRspBodyVO(count, "删除客户家庭成员失败");
    }

}