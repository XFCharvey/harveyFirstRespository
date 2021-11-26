package com.cbox.business.timetask.mock.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.timetask.mock.mapper.ActivityTaskMapper;

@Service
public class ActivityTaskService extends BaseService {

    @Autowired
    ActivityTaskMapper activityTaskMapper;

    public void activityStatusUp() {
        // TODO Auto-generated method stub
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = df.format(date);
        List<Map<String, Object>> listOverdueActivity = activityTaskMapper.listOverdueActivity();
        int actNum = listOverdueActivity.size();
        if (actNum != 0) {
            for (int i = 0; i < listOverdueActivity.size(); i++) {
                Map<String, Object> mapAct = listOverdueActivity.get(i);
                Map<String, Object> actParam = new HashMap<String, Object>();
                String actStatus = StrUtil.getMapValue(mapAct, "act_status");
                String begintime = StrUtil.getMapValue(mapAct, "begin_time");
                String endtime = StrUtil.getMapValue(mapAct, "end_time");
                int begin = begintime.compareTo(nowtime);
                int ending = endtime.compareTo(nowtime);
                if ("0".equals(actStatus)) {
                    if (begin <= 0 && ending > 0) {
                        actParam.put("act_status", "1");
                        actParam.put("rec_updateperson", "admin");
                        Map<String, Object> actCondition = new HashMap<String, Object>();
                        actCondition.put("rec_id", mapAct.get("rec_id"));
                        this.update(actCondition, "d_activity", actParam);
                    }
                    if (begin <= 0 && ending <= 0) {
                        actParam.put("act_status", "2");
                        actParam.put("rec_updateperson", "admin");
                        Map<String, Object> actCondition = new HashMap<String, Object>();
                        actCondition.put("rec_id", mapAct.get("rec_id"));
                        this.update(actCondition, "d_activity", actParam);
                    }
                } else if ("1".equals(actStatus)) {
                    if (ending <= 0) {
                        actParam.put("act_status", "2");
                        actParam.put("rec_updateperson", "admin");
                        Map<String, Object> actCondition = new HashMap<String, Object>();
                        actCondition.put("rec_id", mapAct.get("rec_id"));
                        this.update(actCondition, "d_activity", actParam);
                    }
                }
            }
        } else {
            return;
        }

    }

}
