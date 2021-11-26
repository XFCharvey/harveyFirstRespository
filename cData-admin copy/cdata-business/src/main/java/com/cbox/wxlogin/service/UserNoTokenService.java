package com.cbox.wxlogin.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;
import com.cbox.wxlogin.mapper.UserNoTokenMapper;

@Service
@Transactional
public class UserNoTokenService extends BaseService {

    @Autowired
    UserNoTokenMapper userNoTokenMapper;

    /** updateUser : 注册时补全用户信息 **/
    public ResponseBodyVO updateUser(Map<String, Object> param) {
        int count = 0;
        Map<String, Object> userParam = new HashMap<String, Object>();
        userParam.put("dept_id", param.get("dept_id"));
        userParam.put("real_name", param.get("real_name"));
        userParam.put("id_number", param.get("id_number"));
        userParam.put("identity_type", param.get("identity_type"));
        userParam.put("sex", param.get("sex"));
        userParam.put("status", "2");
        Map<String, Object> userIDCondition = new HashMap<String, Object>();
        userIDCondition.put("id_number", StrUtil.getMapValue(param, "id_number"));
        List<Map<String, Object>> listUserByID = userNoTokenMapper.listUser(userIDCondition);
        if (listUserByID.size() == 0) {
            Map<String, Object> userConditions = new HashMap<String, Object>();
            userConditions.put("user_id", param.get("user_id"));
            userConditions.put("status", "5");// 更新条件增加 5-未提交资料
            count += this.updateNoRec(userConditions, "sys_user", userParam);
            // 发送消息
            Map<String, Object> noticeParam = new HashMap<String, Object>();
            noticeParam.put("notice_title", "补全信息等待审核 · " + param.get("real_name"));
            noticeParam.put("notice_type", "check");
            noticeParam.put("relation_id", param.get("user_id"));
            noticeParam.put("notice_content", param.get("real_name") + "的个人信息补全等待审核处理");
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowtime = df.format(date);
            noticeParam.put("notice_time", nowtime);
            noticeParam.put("notice_dept", param.get("dept_id"));
            count += this.saveNoRec("d_notice", noticeParam, false);

            // 添加审核信息
            Map<String, Object> mapCheckParam = new HashMap<String, Object>();
            mapCheckParam.put("dept_id", param.get("dept_id"));
            mapCheckParam.put("check_type", "user");
            mapCheckParam.put("relation_id", param.get("user_id"));
            mapCheckParam.put("check_title", param.get("real_name") + "的个人信息补全等待审核处理");
            mapCheckParam.put("check_desc", param.get("real_name") + "提交了个人信息补全，等待审核");
            mapCheckParam.put("publish_unitid", param.get("dept_id"));
            mapCheckParam.put("publish_time", nowtime);
            mapCheckParam.put("rec_person", "sys");
            mapCheckParam.put("rec_updateperson", "sys");
            count += this.save("d_check_info", mapCheckParam);

        } else {
            return ServerRspUtil.formRspBodyVO(count, "身份证已存在，请确认后重新输入");
        }

        return ServerRspUtil.formRspBodyVO(count, "修改用户数据失败");
    }

}
