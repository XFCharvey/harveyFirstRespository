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
 * @ClassName: RuleSignHouseService 
 * @Function: 签约后的触发规则，签约1个月，3个月...
 * 
 * @author qiuzq 
 * @date 2021年10月29日 上午10:27:33 
 * @version 1.0
 */
@Service
public class RuleSignHouseService extends BaseService {

    @Autowired
    RuleCommonService ruleCommonService;

    @Autowired
    RuleCommonMapper ruleCommonMapper;

    public void callSignRule() {

        // 获取签约30天(1个月)到1年(370天)的数据
        Map<String, Object> mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listHouse = ruleCommonMapper.getHouseBySign(mapParam);

        // 签约1个月
        String subType = "1";
        this.doSignRule(subType, listHouse);

        // 签约3个月
        subType = "3";
        this.doSignRule(subType, listHouse);

    }

    private void doSignRule(String subType, List<Map<String, Object>> listHouse) {

        String ruleType = "sign";

        Map<String, Map<String, Object>> mapRule = ruleCommonService.getRuleTrigger(ruleType, subType);

        // 遍历签约数据，过滤掉触发表已经触发过的数据，进行规则触发
        for (Map<String, Object> mapObj : listHouse) {
            String objId = StrUtil.getMapValue(mapObj, "rec_id");

            String signDay = StrUtil.getMapValue(mapObj, "contract_day");
            int iDay = StrUtil.getNotNullIntValue(signDay);

            // 根据时间规则进行过滤
            if ("1".equals(subType)) {
                if (iDay < 30 && iDay > 40) {
                    continue;
                }
            } else if ("3".equals(subType)) {
                if (iDay < 90 && iDay > 100) {
                    continue;
                }
            }

            // 判断是否触发过
            if (mapRule.containsKey(objId)) {
                continue;
            }

            // 获取该房子的所有用户，往用户的手机号码发送短信
            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("house_id", objId);
            List<Map<String, Object>> listObjDetail = this.queryNoRec("v_customer_house", mapParam);

            int iCount = 0;
            if (ObjUtil.isNotNull(listObjDetail)) {

                List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
                Map<String, Object> mapRuleData = new HashMap<String, Object>();
                mapRuleData.put("rule_type", ruleType);
                mapRuleData.put("sub_type", subType);
                mapRuleData.put("trigger_obj", objId);

                for (Map<String, Object> map : listObjDetail) {
                    String phoneNo = StrUtil.getMapValue(map, "customer_phone");
                    String msg = "尊敬的准业主，恭喜您已签约" + subType + "个月，请您反馈签约感受，谢谢！点击链接填写  http://cdata.simplesty.com/xxx";

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
