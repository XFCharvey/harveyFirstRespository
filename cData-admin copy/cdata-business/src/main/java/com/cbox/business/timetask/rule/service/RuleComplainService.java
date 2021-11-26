package com.cbox.business.timetask.rule.service;

import java.util.ArrayList;
import java.util.Calendar;
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
 * @ClassName: RuleComplainService 
 * @Function: 投诉工单自动触发任务单，2天后，5天后...
 * 
 * @author qiuzq 
 * @date 2021年10月29日 上午10:27:33 
 * @version 1.0
 */
@Service
public class RuleComplainService extends BaseService {

    @Autowired
    RuleCommonService ruleCommonService;

    @Autowired
    RuleCommonMapper ruleCommonMapper;

    public void callSendTask() {

        // 获取最近10天的投诉数据（按创建时间算）
        Map<String, Object> mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listComplain = ruleCommonMapper.getComplainTask(mapParam);

        // 2天后
        String subType = "2";
        this.doSendTask(subType, listComplain);

        // 5天后
        subType = "5";
        this.doSendTask(subType, listComplain);

    }

    private void doSendTask(String subType, List<Map<String, Object>> listComplain) {

        String ruleType = "task";

        Map<String, Map<String, Object>> mapRule = ruleCommonService.getRuleTrigger(ruleType, subType);

        // 遍历投诉数据，过滤掉触发表已经触发过的数据，进行规则触发
        for (Map<String, Object> mapObj : listComplain) {
            String objId = StrUtil.getMapValue(mapObj, "rec_id");

            String signDay = StrUtil.getMapValue(mapObj, "diff_day");
            int iDay = StrUtil.getNotNullIntValue(signDay);

            // 根据时间规则进行过滤
            if ("2".equals(subType)) {
                if (iDay != 2) {
                    continue;
                }
            } else if ("5".equals(subType)) {
                if (iDay != 5) {
                    continue;
                }
            }

            // 判断是否触发过
            if (mapRule.containsKey(objId)) {
                continue;
            }

            /* 给当前处理人派发任务工单 */
            String dealPerson = StrUtil.getMapValue(mapObj, "deal_person");
            String taskName = "【投诉" + subType + "天后维护】" + StrUtil.getMapValue(mapObj, "task_name");
            String taskDetail = "原投诉内容：" + StrUtil.getMapValue(mapObj, "task_detail") + "  \n房源号：" + StrUtil.getMapValue(mapObj, "task_object") + "  \n" + StrUtil.getMapValue(mapObj, "deal_people");

            String projectId = StrUtil.getMapValue(mapObj, "project_id");
            String housesId = StrUtil.getMapValue(mapObj, "houses_id");

            if (StrUtil.isNull(housesId)) {
                // 如果为空，重新匹配一次
                Map<String, Object> mapHouseParam = new HashMap<String, Object>();
                mapHouseParam.put("house_name", StrUtil.getMapValue(mapObj, "task_object"));
                List<Map<String, Object>> listHouseGet = this.query("d_project_houses", mapHouseParam);

                if (ObjUtil.isNotNull(listHouseGet)) {
                    Map<String, Object> map = listHouseGet.get(0);
                    projectId = StrUtil.getMapValue(map, "project_id");
                    housesId = StrUtil.getMapValue(map, "rec_id");
                }
            }

            Map<String, Object> mapParam = new HashMap<String, Object>();
            if (StrUtil.isNotNull(projectId)) {
                mapParam.put("project_id", projectId);
            }
            if (StrUtil.isNotNull(housesId)) {
                mapParam.put("houses_id", housesId);
            }

            mapParam.put("task_name", taskName);
            mapParam.put("task_type", "task");
            mapParam.put("task_status", "0");
            mapParam.put("relate_taskid", StrUtil.getMapValue(mapObj, "rec_id"));
            mapParam.put("task_detail", taskDetail);

            mapParam.put("deal_person", dealPerson);

            // 默认：3天的处理时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            Date newDate = calendar.getTime();
            mapParam.put("task_deadline", DateUtils.format(newDate, DateUtils.YYYY_MM_DD_HH_MM_SS));

            mapParam.put("rec_person", "task");
            mapParam.put("rec_updateperson", "task");
            int count = this.save("d_worktask", mapParam);

            /* 记录到触发表中 */
            int iCount = 1;
            List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
            Map<String, Object> mapRuleData = new HashMap<String, Object>();
            mapRuleData.put("rule_type", ruleType);
            mapRuleData.put("sub_type", subType);
            mapRuleData.put("trigger_obj", objId);
            mapRuleData.put("trigger_num", iCount);

            String msg = "自动派发" + subType + "天后投诉跟踪任务单";
            Map<String, Object> mapDetail = new HashMap<String, Object>();
            mapDetail.put("detail_key", dealPerson);
            mapDetail.put("detail_descr", msg);
            listDetail.add(mapDetail);

            ruleCommonService.saveRuleTrigger(mapRuleData, listDetail);

        }
    }

}
