package com.cbox.business.email.email.service;

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
import com.cbox.business.email.email.mapper.EmailMapper;


/**
 * @ClassName: EmailService
 * @Function: 邮件表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class EmailService extends BaseService {

    @Autowired
    private EmailMapper emailMapper;

	/** listEmail : 获取邮件表列表数据 **/
	public List<Map<String, Object>> listEmail(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = emailMapper.listEmail(param);

		return list;
	}

	/** getEmail : 获取指定id的邮件表数据 **/
	public ResponseBodyVO getEmail(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = emailMapper.getEmail(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addEmail : 新增邮件表 **/
	public ResponseBodyVO addEmail(Map<String, Object> param) {

		// Table:d_email
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("email_url", param.get("email_url"));
		mapParam.put("email_owner", param.get("email_owner"));
		mapParam.put("owner_desc", param.get("owner_desc"));
		int count = this.save( "d_email", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增邮件表失败");
	}

	/** updateEmail : 修改邮件表 **/
	public ResponseBodyVO updateEmail(Map<String, Object> param) {

		// Table:d_email
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("email_url", param.get("email_url"));
		mapParam.put("email_owner", param.get("email_owner"));
		mapParam.put("owner_desc", param.get("owner_desc"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_email", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改邮件表失败"); 
	}

	/** delEmail : 删除邮件表 **/
	public ResponseBodyVO delEmail(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_email
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_email",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除邮件表失败");
	}

}
