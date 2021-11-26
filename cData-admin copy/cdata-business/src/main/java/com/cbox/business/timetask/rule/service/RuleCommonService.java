package com.cbox.business.timetask.rule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.timetask.rule.mapper.RuleCommonMapper;

/**
 * @ClassName: RuleActivityService 
 * @Function: 触发活动规则  
 * 
 * @author qiuzq 
 * @date 2021年10月29日 上午10:27:33 
 * @version 1.0
 */
@Service
public class RuleCommonService extends BaseService {

    @Autowired
    RuleCommonMapper ruleCommonMapper;

    /**
     * getRuleTrigger: 根据类型得到规则触发的记录
     *
     * @date: 2021年10月29日 下午3:09:12 
     * @author qiuzq 
     * @param ruleType
     * @return
     */
    public Map<String, Map<String, Object>> getRuleTrigger(String ruleType) {
        return this.getRuleTrigger(ruleType, "");
    }

    public Map<String, Map<String, Object>> getRuleTrigger(String ruleType, String subType) {

        // 获取触发表数据
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("rule_type", ruleType);// 类型是活动
        if (StrUtil.isNotNull(subType)) {
            mapParam.put("sub_type", subType);
        }
        List<Map<String, Object>> listRuleAct = this.queryNoRec("d_rule_trigger", mapParam);
        Map<String, Map<String, Object>> mapRuleTrigger = ObjUtil.transListToMap(listRuleAct, "trigger_obj");

        return mapRuleTrigger;
    }

    /**
     * saveRuleTrigger: 保存触发记录
     *
     * @date: 2021年10月29日 下午3:47:37 
     * @author qiuzq 
     * @param mapRuleData
     * @param listDetail
     * @return
     */
    public boolean saveRuleTrigger(Map<String, Object> mapRuleData, List<Map<String, Object>> listDetail) {

        // 保存规则触发表
        mapRuleData.put("trigger_time", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));// 触发时间
        int count = this.saveNoRec("d_rule_trigger", mapRuleData, true);

        String ruleRedId = StrUtil.getMapValue(mapRuleData, "rec_id");

        // 保存规则触发详情表
        int iBatch = 0;
        int iIndex = 0;
        List<Map<String, Object>> listBatchParam = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> mapDetail : listDetail) {

            listBatchParam.add(mapDetail);

            iBatch++;
            iIndex++;
            if (iIndex == listDetail.size() || iBatch > 90) {
                if (ObjUtil.isNotNull(listBatchParam)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("rule_trigger_id", ruleRedId);
                    map.put("rule_type", StrUtil.getMapValue(mapRuleData, "rule_type"));
                    map.put("list", listBatchParam);
                    ruleCommonMapper.insertRuleDetailBatch(map);

                    listBatchParam = new ArrayList<Map<String, Object>>();
                    iBatch = 0;
                }
            }
        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /** 与当前时间比较，计算时间间隔  */
    public int calcInterval(String endTime, String type) {

        Date nowDate = new Date();// 当前时间
        long nowTime = nowDate.getTime();
        long lastTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, endTime).getTime();// 以前的时间
        long time = nowTime - lastTime;// 时间相减比较。

        int iInterval = 0;
        if ("day".equals(type)) {
            iInterval = (int) (time / (1000 * 60 * 60 * 24)); // 计算出天间隔
        } else if ("hour".equals(type)) {
            iInterval = (int) (time / (1000 * 60 * 60)); // 计算出小时间隔
        } else {
            iInterval = (int) (time / (1000 * 60 * 60)); // 计算出小时间隔
        }

        return iInterval;
    }

}
