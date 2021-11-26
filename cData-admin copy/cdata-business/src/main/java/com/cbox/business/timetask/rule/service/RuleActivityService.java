package com.cbox.business.timetask.rule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;

/**
 * @ClassName: RuleActivityService 
 * @Function: 触发活动规则  
 * 
 * @author qiuzq 
 * @date 2021年10月29日 上午10:27:33 
 * @version 1.0
 */
@Service
public class RuleActivityService extends BaseService {

    @Autowired
    RuleCommonService ruleCommonService;

    public void callQuestion() {

        // 获取活动数据
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("act_status", "2");// 已结束的活动
        mapParam.put("act_type", "offline");
        List<Map<String, Object>> listActivity = this.queryNoRec("d_activity", mapParam);

        // 获取触发表数据
        String ruleType = "act";
        Map<String, Map<String, Object>> mapRuleAct = ruleCommonService.getRuleTrigger(ruleType);

        // 遍历活动数据，过滤掉触发表已经触发过的数据，进行规则触发
        for (Map<String, Object> mapAct : listActivity) {
            String actId = StrUtil.getMapValue(mapAct, "rec_id");
            String questionId = StrUtil.getMapValue(mapAct, "question_id");

            // 判断是否绑定问卷
            if (StrUtil.isNull(questionId)) {
                System.err.println("活动[" + actId + "]未绑定任何问卷，无法自动触发问卷调研！");
                continue;
            }

            // 判断是否超过6小时
            String endTime = StrUtil.getMapValue(mapAct, "end_time");
            if (StrUtil.isNull(endTime)) {
                System.err.println("活动[" + actId + "]活动结束时间为空，无法自动触发问卷调研！");
                continue;
            }

            int iInterval = ruleCommonService.calcInterval(endTime, "hour");
            if (iInterval < 6) {
                continue;
            }

            // 判断是否触发过
            if (mapRuleAct.containsKey(actId)) {
                continue;
            }

            // 获取该问卷的所有签到用户，然后发送短信
            mapParam = new HashMap<String, Object>();
            mapParam.put("activity_id", actId);
            List<Map<String, Object>> listActSign = this.queryNoRec("d_activity_signin", mapParam);

            int iCount = 0;
            if (ObjUtil.isNotNull(listActSign)) {

                List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
                Map<String, Object> mapRuleData = new HashMap<String, Object>();
                mapRuleData.put("rule_type", ruleType);
                mapRuleData.put("sub_type", "");
                mapRuleData.put("trigger_obj", actId);

                for (Map<String, Object> mapSign : listActSign) {
                    String phoneNo = StrUtil.getMapValue(mapSign, "signin_phone");
                    String msg = "尊敬的客户感谢您参与活动，请您花几分钟时间对活动进行反馈，谢谢！点击链接填写反馈问卷：http://cdata.simplesty.com/xxx";

                    Map<String, Object> mapDetail = new HashMap<String, Object>();
                    mapDetail.put("detail_key", phoneNo);
                    mapDetail.put("detail_descr", msg);
                    listDetail.add(mapDetail);

                    iCount++;

                    System.out.println(actId + "," + questionId + "," + phoneNo);

                }

                mapRuleData.put("trigger_num", iCount);

                ruleCommonService.saveRuleTrigger(mapRuleData, listDetail);

                System.out.println(actId + "," + "Total：" + iCount);
            }


        }

    }

}
