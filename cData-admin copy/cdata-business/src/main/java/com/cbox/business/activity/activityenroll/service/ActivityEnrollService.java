package com.cbox.business.activity.activityenroll.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.activity.activity.mapper.ActivityMapper;
import com.cbox.business.activity.activityenroll.mapper.ActivityEnrollMapper;


/**
 * @ClassName: ActivityEnrollService
 * @Function: 活动报名登记
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class ActivityEnrollService extends BaseService {

    @Autowired
    private ActivityEnrollMapper activityEnrollMapper;
    
    @Autowired
    ActivityMapper activityMapper;

	/** listActivityEnroll : 获取活动报名登记列表数据 **/
	public List<Map<String, Object>> listActivityEnroll(Map<String, Object> param) {
		super.appendUserInfo(param);
        String loginDept = SecurityUtils.getLoginUser().getUser().getDeptId().toString();
        param.put("dept_id", loginDept);
		List<Map<String, Object>> list = activityEnrollMapper.listActivityEnroll(param);

		return list;
	}

	/** getActivityEnroll : 获取指定id的活动报名登记数据 **/
	public ResponseBodyVO getActivityEnroll(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = activityEnrollMapper.getActivityEnroll(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addActivityEnroll : 新增活动报名登记 **/
	public ResponseBodyVO addActivityEnroll(Map<String, Object> param) {
        Map<String, Object> mapResult = activityEnrollMapper.getActivityEnroll(param);
        if (mapResult != null) {
            return ServerRspUtil.formRspBodyVO(0, "目前用户已登记报名该活动");
        } else {
            int count = 0;
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowtime = df.format(date);
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("activity_id", param.get("activity_id"));
            mapParam.put("e_name", param.get("e_name"));
            mapParam.put("e_tell", param.get("e_tell"));
            mapParam.put("e_dept", param.get("e_dept"));
            mapParam.put("e_hope", param.get("e_hope"));
            mapParam.put("e_time", nowtime);
            mapParam.put("e_person", param.get("e_person"));
            Map<String, Object> actParam = new HashMap<String, Object>();
            actParam.put("rec_id", param.get("activity_id"));
            List<Map<String, Object>> actresult = activityMapper.listActivity(actParam);
            int invitation_num = 0;
            for (int i = 0; i < actresult.size(); i++) {
                Map<String, Object> actMap = actresult.get(i);
                invitation_num = StrUtil.getMapIntValue(actMap, "invitation_num");
                String auto_check = StrUtil.getMapValue(actMap, "auto_check");
                System.out.println(auto_check);
                if ("1".equals(auto_check)) {
                    mapParam.put("e_status", "1");
                } else {
                    mapParam.put("e_status", "0");
                }
            }
            count = this.saveNoRec("d_activity_enroll", mapParam, false);

            double joinHead = 0.0;
            Map<String, Object> enrollParam = new HashMap<String, Object>();
            enrollParam.put("activity_id", param.get("activity_id"));
            List<Map<String, Object>> listNowEnroll = activityEnrollMapper.listActivityEnroll(enrollParam);
            int alljoinNum = listNowEnroll.size();
            if (invitation_num != 0) {
                joinHead = new BigDecimal((float) alljoinNum / invitation_num).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            } else {
                joinHead = 0;
            }

            Map<String, Object> joinHeadParam = new HashMap<String, Object>();
            joinHeadParam.put("join_degree", joinHead);
            System.out.println(joinHeadParam);
            count = this.update(actParam, "d_activity", joinHeadParam);



            return ServerRspUtil.formRspBodyVO(count, "新增活动报名登记失败");

        }


	}

	/** updateActivityEnroll : 修改活动报名登记 **/
    public ResponseBodyVO updateActivityEnroll(Map<String, Object> param) {
        int count = 0;
        String nowTime = DateUtils.getTime();
        String userName = SecurityUtils.getUsername();
		// Table:d_activity_enroll
        String recId = StrUtil.getMapValue(param, "rec_id");

        String[] recIds = recId.split(",");// 可能是批量审批
        for (int i = 0; i < recIds.length; i++) {
            // 更新字段
            Map<String, Object> mapParam = new HashMap<String, Object>();
            StrUtil.setMapValue(mapParam, "e_status", param);
            StrUtil.setMapValue(mapParam, "check_person", userName);
            StrUtil.setMapValue(mapParam, "check_time", nowTime);
            StrUtil.setMapValue(mapParam, "check_reject_reason", param);

            // 条件
            Map<String, Object> mapCondition = new HashMap<String, Object>();
            mapCondition.put("rec_id", recIds[i]);
            count = this.updateNoRec(mapCondition, "d_activity_enroll", mapParam);
        }

        return ServerRspUtil.formRspBodyVO(count, "修改活动报名成功");

	}

	/** delActivityEnroll : 删除活动报名登记 **/
	public ResponseBodyVO delActivityEnroll(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_activity_enroll
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.deleteEmpty("d_activity_enroll", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除活动报名登记失败");
	}


}
