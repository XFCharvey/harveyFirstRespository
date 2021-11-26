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
import com.cbox.business.timetask.rule.mapper.RuleCommonMapper;

/**
 * @ClassName: RuleHouseService 
 * @Function: 交房当日晚上9点，触发交房调研  
 * 
 * @author qiuzq 
 * @date 2021年10月29日 上午10:27:33 
 * @version 1.0
 */
@Service
public class RuleHouseService extends BaseService {

    @Autowired
    RuleCommonService ruleCommonService;

    @Autowired
    RuleCommonMapper ruleCommonMapper;

    public void callQuestion() {

        // 获取最近10天的交房数据
        Map<String, Object> mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listHouse = ruleCommonMapper.getHouseByDeliver(mapParam);

        // 获取触发表数据
        String ruleType = "house";
        Map<String, Map<String, Object>> mapRule = ruleCommonService.getRuleTrigger(ruleType);

        // 遍历交房数据，过滤掉触发表已经触发过的数据，进行规则触发
        for (Map<String, Object> mapObj : listHouse) {
            String objId = StrUtil.getMapValue(mapObj, "rec_id");

            // 判断是否触发过
            if (mapRule.containsKey(objId)) {
                continue;
            }

            // 获取该房子的所有用户，往用户的手机号码发送短信
            mapParam = new HashMap<String, Object>();
            mapParam.put("house_id", objId);
            List<Map<String, Object>> listObjDetail = this.queryNoRec("v_customer_house", mapParam);

            int iCount = 0;
            if (ObjUtil.isNotNull(listObjDetail)) {

                List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
                Map<String, Object> mapRuleData = new HashMap<String, Object>();
                mapRuleData.put("rule_type", ruleType);
                mapRuleData.put("sub_type", "");
                mapRuleData.put("trigger_obj", objId);

                for (Map<String, Object> map : listObjDetail) {
                    String phoneNo = StrUtil.getMapValue(map, "customer_phone");
                    String msg = "恭喜您成为融创业主，请您花几分钟时间反馈收房感受，谢谢！点击链接填写  http://cdata.simplesty.com/xxx";

                    Map<String, Object> mapDetail = new HashMap<String, Object>();
                    mapDetail.put("detail_key", phoneNo);
                    mapDetail.put("detail_descr", msg);
                    listDetail.add(mapDetail);

                    iCount++;

                    System.out.println(objId + "," + phoneNo);

                }

                mapRuleData.put("trigger_num", iCount);

                ruleCommonService.saveRuleTrigger(mapRuleData, listDetail);

                System.out.println(objId + "," + "Total：" + iCount);
            }


        }

    }

}
