package com.cbox.business.user.userservehis.service;

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
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.business.user.userservehis.mapper.UserServehisMapper;


/**
 * @ClassName: UserServehisService
 * @Function: 员工用户服务历史
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class UserServehisService extends BaseService {

    @Autowired
    private UserServehisMapper userServehisMapper;
    
	/** listUserServehis : 获取员工用户服务历史列表数据 **/
	public List<Map<String, Object>> listUserServehis(Map<String, Object> param) {
		super.appendUserInfo(param);

        String userName = SecurityUtils.getUsername();
        param.put("serve_person", userName);
		List<Map<String, Object>> list = userServehisMapper.listUserServehis(param);

		return list;
	}

	/** getUserServehis : 获取指定id的员工用户服务历史数据 **/
	public ResponseBodyVO getUserServehis(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = userServehisMapper.getUserServehis(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addUserServehis : 新增员工用户服务历史 **/
	public ResponseBodyVO addUserServehis(Map<String, Object> param) {
        String userName = SecurityUtils.getUsername();
        String nowTime = DateUtils.getTime();
        int count = 0;
		// Table:d_user_servehis
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("serve_person", userName);
		mapParam.put("served_customer", param.get("served_customer"));
        mapParam.put("serve_time", nowTime);

        Map<String, Object> mapUserServeHisCondition = new HashMap<String, Object>();
        mapUserServeHisCondition.put("serve_person", userName);
        mapUserServeHisCondition.put("served_customer", param.get("served_customer"));
        List<Map<String, Object>> listServeHis = userServehisMapper.listUserServehis(mapUserServeHisCondition);
        if (ObjUtil.isNotNull(listServeHis) || listServeHis.size() > 0) {
            count += this.update(mapUserServeHisCondition, "d_user_servehis", mapParam);
            Map<String, Object> mapUserServeUpdateParam = new HashMap<String, Object>();
            mapUserServeUpdateParam.put("serve_count", 1);
            count += this.updateIncrement(mapUserServeHisCondition, "d_user_servehis", mapUserServeUpdateParam);
        } else {
            mapParam.put("serve_count", "1");
            count += this.save("d_user_servehis", mapParam);

        }

		return ServerRspUtil.formRspBodyVO(count, "新增员工用户服务历史失败");
	}

	/** updateUserServehis : 修改员工用户服务历史 **/
	public ResponseBodyVO updateUserServehis(Map<String, Object> param) {

		// Table:d_user_servehis
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("serve_person", param.get("serve_person"));
		mapParam.put("served_customer", param.get("served_customer"));
		mapParam.put("serve_count", param.get("serve_count"));
		mapParam.put("serve_time", param.get("serve_time"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_user_servehis", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改员工用户服务历史失败"); 
	}

	/** delUserServehis : 删除员工用户服务历史 **/
	public ResponseBodyVO delUserServehis(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_user_servehis
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_user_servehis",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除员工用户服务历史失败");
	}


}
