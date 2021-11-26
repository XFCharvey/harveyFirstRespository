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
 * @ClassName: RuleProjectStatusService 
 * @Function: 计算项目及节点状态  
 * 
 * @author qiuzq 
 * @date 2021年10月29日 上午10:27:33 
 * @version 1.0
 */
@Service
public class RuleProjectStatusService extends BaseService {

    @Autowired
    RuleCommonService ruleCommonService;

    @Autowired
    RuleCommonMapper ruleCommonMapper;

    public void refreshStatus() {

        // 获取未完成的项目
        Map<String, Object> mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listProjectNoEnd = ruleCommonMapper.getProjectNoEnd(mapParam);

        // 获取项目节点
        mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listProjectNode = this.query("d_project_node", mapParam);
        Map<String, List<Map<String, Object>>> mapListProjectNode = ObjUtil.transList(listProjectNode, "project_id");

        // 遍历
        for (Map<String, Object> mapObj : listProjectNoEnd) {
            String objId = StrUtil.getMapValue(mapObj, "rec_id"); // 项目id

            List<Map<String, Object>> listCurrNode = mapListProjectNode.get(objId);

            int iPrePass = 0;
            int iPass = 0;
            int iMorePass = 0;
            int iFinish = 0;

            Map<String, Object> mapProParams = new HashMap<String, Object>();
            // 项目状态：0-未开始，1-进行中，2-进度异常，3-严重滞后，4-已完成，5-延期完成，6-超时完成
            String projectStatus = "1";
            String lastTime = "";
            if (ObjUtil.isNull(listCurrNode)) {
                projectStatus = "0"; // 未开始
            } else {
                List<Map<String, Object>> listUpdateNode = new ArrayList<Map<String, Object>>();
                // 遍历节点
                for (int i = 0; i < listCurrNode.size(); i++) {
                    Map<String, Object> mapNode = listCurrNode.get(i);

                    String nodeId = StrUtil.getMapValue(mapNode, "rec_id");
                    String nodeStatus = StrUtil.getMapValue(mapNode, "node_status");
                    // 节点状态：0-未完成，1-即将超时，2-超时，3-严重超时，4-正常完成，5-延期完成，6-超时完成
                    Integer iStatus = StrUtil.getNotNullIntValue(nodeStatus);
                    if (iStatus >= 4) {
                        iFinish++;
                        continue;
                    }

                    String endTime = StrUtil.getNotNullStrValue(StrUtil.getMapValue(mapNode, "delay_deadline"), StrUtil.getMapValue(mapNode, "node_deadline"));
                    if (StrUtil.isNull(endTime)) {
                        continue;
                    }

                    if (i == listCurrNode.size() - 1) {
                        lastTime = endTime;
                    }

                    int iInterval = ruleCommonService.calcInterval(endTime, "day");
                    // 即将超时-提前5天；超时-小于10天；严重超时-大于10天
                    if (iInterval > 10) {
                        nodeStatus = "3";// 严重超时
                        iMorePass++;
                    } else if (iInterval > 0 && iInterval <= 10) {
                        nodeStatus = "2";// 超时
                        iPass++;
                    } else if (iInterval >= -5 && iInterval <= 0) {
                        nodeStatus = "1";// 即将超时
                        iPrePass++;
                    }

                    // 更新节点表状态
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("node_status", nodeStatus);
                    params.put("rec_id", nodeId);
                    listUpdateNode.add(params);
                }

                // 批量更新节点信息
                if (ObjUtil.isNotNull(listUpdateNode)) {
                    Map<String, Object> mapBatch = new HashMap<String, Object>();
                    mapBatch.put("list", listUpdateNode);
                    int count = ruleCommonMapper.updateProjctNodeBatch(mapBatch);
                }

                String endTime = StrUtil.getNotNullStrValue(StrUtil.getMapValue(mapObj, "end_time"), lastTime);
                // 说明全部节点都完成了
                if (iFinish == listCurrNode.size()) {

                    int iInterval = ruleCommonService.calcInterval(endTime, "day");
                    if (iInterval > 0) {
                        projectStatus = "6";// 超时完成
                    } else {
                        projectStatus = "4";// 正常完成
                    }

                    // todo...现在不能标识出项目的延期完成！

                } else {
                    if (iMorePass > 0) {
                        projectStatus = "3";// 严重滞后
                    } else if (iPass > 0) {
                        projectStatus = "2";// 进度异常
                    }
                }

                mapProParams.put("node_finish_num", iFinish);// 更新节点完成数
                if (StrUtil.isNull(StrUtil.getMapValue(mapObj, "end_time"))) {
                    mapProParams.put("end_time", endTime);
                }
            }

            // 更新项目表状态
            mapProParams.put("project_status", projectStatus);
            Map<String, Object> mapCondition = new HashMap<String, Object>();
            mapCondition.put("rec_id", StrUtil.getMapValue(mapObj, "rec_id"));
            int count = this.updateNoRec(mapCondition, "d_project", mapProParams);


        }

    }

}
