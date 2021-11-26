package com.cbox.business.customer.customertag.service;

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
import com.cbox.business.customer.customertag.mapper.CustomerTagMapper;

/**
 * @ClassName: CustomerTagService
 * @Function: 客户标签
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerTagService extends BaseService {

	@Autowired
	private CustomerTagMapper customerTagMapper;

	/** listCustomerTag : 获取客户标签列表数据 **/
	public List<Map<String, Object>> listCustomerTag(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = customerTagMapper.listCustomerTag(param);

		return list;
	}

	/** getCustomerTag : 获取指定id的客户标签数据 **/
	public ResponseBodyVO getCustomerTag(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = customerTagMapper.getCustomerTag(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCustomerTag : 新增客户标签 **/
	public ResponseBodyVO addCustomerTag(Map<String, Object> param) {

		List<Map<String, Object>> list = customerTagMapper.listCustomerTag(new HashMap<String, Object>());

		// Table:d_customer_tag
		Map<String, Object> mapParam = new HashMap<String, Object>();
		String tagName = StrUtil.getMapValue(param, "tag_name");

		// 检查是否有相同标签名
		boolean existFlag = false;
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tagParam = list.get(i);
				String tagFormCacheName = StrUtil.getMapValue(tagParam, "dictLabel");
				if (tagFormCacheName.equals(tagName)) {
					existFlag = true;
					break;
				}
			}
		}

		if (existFlag) {
			return ServerRspUtil.formRspBodyVO(-1, "新增客户标签失败,存在相同标签");
		} else {
			mapParam.put("tag_name", tagName);
			mapParam.put("tag_desc", param.get("tag_desc"));
			int count = this.save("d_customer_tag", mapParam);
			return ServerRspUtil.formRspBodyVO(count, "新增客户标签失败");
		}
	}

	/** updateCustomerTag : 修改客户标签 **/
	public ResponseBodyVO updateCustomerTag(Map<String, Object> param) {

		List<Map<String, Object>> list = customerTagMapper.listCustomerTag(new HashMap<String, Object>());

		Map<String, Object> mapSelf = customerTagMapper.getCustomerTag(param);

		String selfName = StrUtil.getMapValue(mapSelf, "dictLabel");

		// Table:d_customer_tag
		Map<String, Object> mapParam = new HashMap<String, Object>();
		String tagName = StrUtil.getMapValue(param, "tag_name");
		// 检查是否有相同标签名
		boolean existFlag = false;
		if (ObjUtil.isNotNull(list)) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tagParam = list.get(i);
				String tagFormCacheName = StrUtil.getMapValue(tagParam, "dictLabel");
				if (tagFormCacheName.equals(tagName)) {
					existFlag = true;
					break;
				}
			}
		}
		if (selfName.equals(tagName)) {
			existFlag = false;
		}

		if (existFlag) {
			return ServerRspUtil.formRspBodyVO(-1, "修改客户标签失败,存在相同标签");
		} else {
			mapParam.put("tag_name", tagName);
			mapParam.put("tag_desc", param.get("tag_desc"));
			Map<String, Object> mapCondition = new HashMap<String, Object>();
			mapCondition.put("rec_id", param.get("rec_id"));
			int count = this.update(mapCondition, "d_customer_tag", mapParam);
			return ServerRspUtil.formRspBodyVO(count, "修改客户标签失败");
		}
	}

	/** delCustomerTag : 删除客户标签 **/
	public ResponseBodyVO delCustomerTag(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断

		// Table:d_customer_tag
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_customer_tag", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除客户标签失败");
	}

}
