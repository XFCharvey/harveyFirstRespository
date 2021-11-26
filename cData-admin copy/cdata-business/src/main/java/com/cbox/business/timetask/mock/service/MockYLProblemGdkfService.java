package com.cbox.business.timetask.mock.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;
import com.cbox.business.timetask.mock.mapper.MockProjectMapper;
import com.cbox.business.timetask.mock.util.MockHttpsUtil;

/**
 * @ClassName: MockYLProblemGdkfService 
 * @Function: 云链系统，工地开放问题  
 * 
 * @author qiuzq 
 * @date 2021年9月18日 上午10:49:47 
 * @version 1.0
 */
@Service
public class MockYLProblemGdkfService extends BaseService {
    
    @Autowired
    private MockProjectMapper mockProjectMapper;

    @Autowired
    private ProjectHousesMapper projectHousesMapper;


    public static void main(String[] args) {
        MockYLProblemGdkfService gdkfService = new MockYLProblemGdkfService();

        HttpClient httpClient = MockHttpsUtil.mockLoginYl();

        String projectId = "e0c52f93-5398-e711-80b3-e04f43069c1a";
        // gdkfService.getHouseMsg(httpClient, projectId);

        gdkfService.getOpenBatch(httpClient, projectId);

        gdkfService.getHandoverBatch(httpClient, projectId);

    }
    
    public void callProblemGdkf() {
        HttpClient httpClient = MockHttpsUtil.mockLoginYl();
        
        List<Map<String, Object>> listProject = mockProjectMapper.listProject();
        for (int i = 0; i < listProject.size(); i++) {
            Map<String, Object> mapProject = listProject.get(i);
            String projGuid = StrUtil.getMapValue(mapProject, "proj_guid");
            
            // projGuid = "e0c52f93-5398-e711-80b3-e04f43069c1a";

            // 抓取工地问题
            this.getOpenBatch(httpClient, projGuid);

            // 抓取交房问题
            // 抓取交房时间
            this.getHandoverBatch(httpClient, projGuid);

            // break; // 测试需要，运行完第1个项目就跳出
        }
    }
    
    /** 抓取工地开发批次 */
    private void getOpenBatch(HttpClient httpClient, String projectId) {
        String url = "https://yf-hb.myyscm.com/sunac/checkroom/batch/list?_fc=OpeningBatch&_smp=MobileCheckRoom.OpeningBatch";
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("pageSize", "20");
        map.put("projectId", projectId);
        // map.put("_t", "1636271783158");

        String result = MockHttpsUtil.doHttpsPost(httpClient, url, map);

        // 解析数据
        if (StrUtil.isNotNull(result)) {
            JSONObject obj = JSON.parseObject(result);

            JSONArray jArray = obj.parseArray(obj.getString("items"));
            for (int i = 0; i < jArray.size(); i++) {
                JSONObject objItem = jArray.getJSONObject(i);

                String batchPurpose = objItem.getString("batch_purpose");
                if (StrUtil.isNotNull(batchPurpose) && batchPurpose.contains("测试")) {
                    continue;
                }

                String batchId = objItem.getString("id");
                String batchName = objItem.getString("name");
                System.out.println(batchId + "," + batchName);

                this.getOpenProblem(httpClient, projectId, batchId, batchName);
            }
        }
    }



    /** 抓取工地问题 */
    private void getOpenProblem(HttpClient httpClient, String projectId, String batchId, String batchName) {

        // 获取总数量
        String urlCount = "https://yf-hb.myyscm.com/sunac/checkroom/problem-handle/problem-badges?_fc=OpeningProblemHandle&_smp=MobileCheckRoom.OpeningProblemHandle";

        String urlData = "https://yf-hb.myyscm.com/sunac/checkroom/problem-handle/query-problem?_fc=OpeningProblemHandle&_smp=MobileCheckRoom.OpeningProblemHandle";

        this.getProblem(httpClient, projectId, batchId, batchName, urlCount, urlData, "1");

    }

    /** 抓取正式交付的批次 */
    private void getHandoverBatch(HttpClient httpClient, String projectId) {
        String url = "https://yf-hb.myyscm.com/sunac/checkroom/batch/list?_ac=MobileCheckRoom&_fc=DeliverBatch&_smp=MobileCheckRoom.DeliverBatch";

        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("pageSize", "20");
        map.put("projectId", projectId);
        // map.put("_t", "1636271783158");

        String result = MockHttpsUtil.doHttpsPost(httpClient, url, map);

        // 解析数据
        if (StrUtil.isNotNull(result)) {
            JSONObject obj = JSON.parseObject(result);

            JSONArray jArray = obj.parseArray(obj.getString("items"));
            for (int i = 0; i < jArray.size(); i++) {
                JSONObject objItem = jArray.getJSONObject(i);

                String batchPurpose = objItem.getString("batch_purpose");
                if (StrUtil.isNotNull(batchPurpose) && batchPurpose.contains("测试")) {
                    continue;
                }

                String batchId = objItem.getString("id");
                String batchName = objItem.getString("name");
                System.out.println(batchId + "," + batchName);

                this.getHandoverProblem(httpClient, projectId, batchId, batchName);

                this.getHandoverRoom(httpClient, projectId, batchId, batchName);
            }
        }
    }

    /** 抓取正式交付的问题 */
    private void getHandoverProblem(HttpClient httpClient, String projectId, String batchId, String batchName) {

        // 获取总数量
        String urlCount = "https://yf-hb.myyscm.com/sunac/checkroom/problem-handle/problem-badges?_ac=MobileCheckRoom&_fc=DeliverProblemHandle&_smp=MobileCheckRoom.DeliverProblemHandle";

        String urlData = "https://yf-hb.myyscm.com/sunac/checkroom/problem-handle/query-problem?_ac=MobileCheckRoom&_fc=DeliverProblemHandle&_smp=MobileCheckRoom.DeliverProblemHandle";

        this.getProblem(httpClient, projectId, batchId, batchName, urlCount, urlData, "2");

        // // 获取总数量
        // String url =
        // "https://yf-hb.myyscm.com/sunac/checkroom/problem-handle/problem-badges?_ac=MobileCheckRoom&_fc=DeliverProblemHandle&_smp=MobileCheckRoom.DeliverProblemHandle";
        //
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("projectId", projectId);
        // map.put("batchId", batchId);
        // map.put("status", "所有问题");
        // map.put("isDefaultSort", "1");
        // map.put("userType", "regist_user_id");
        // map.put("dateType", "regist_date");
        //
        // String result = MockHttpsUtil.doHttpsPost(httpClient, url, map);
        // System.out.println(result);
        //
        // // 分页获取具体问题数据
        // url = "https://yf-hb.myyscm.com/sunac/checkroom/problem-handle/query-problem?_ac=MobileCheckRoom&_fc=DeliverProblemHandle&_smp=MobileCheckRoom.DeliverProblemHandle";
        // map.put("page", "1");
        // map.put("pageSize", "20");
        // result = MockHttpsUtil.doHttpsPost(httpClient, url, map);
        // System.out.println(result);
        //
        // JSONArray jArray = this.parseResult(result);
        // this.printArray(jArray);
        //
        // // todo...遍历，把正式交付的问题入库

    }

    private void getProblem(HttpClient httpClient, String projectId, String batchId, String batchName, String urlCount, String urlData, String type) {

        String typeName = "工地开放问题";
        if ("2".equals(type)) {
            typeName = "交付问题";
        }

        // 获取总数量
        String url = urlCount;

        Map<String, String> map = new HashMap<String, String>();

        map.put("projectId", projectId);
        map.put("batchId", batchId);
        map.put("status", "所有问题");
        map.put("isDefaultSort", "1");
        map.put("userType", "regist_user_id");
        map.put("dateType", "regist_date");

        String result = MockHttpsUtil.doHttpsPost(httpClient, url, map);
        System.out.println(result);
        int iTotalCount = MockHttpsUtil.parseProblemCount(result);
        System.out.println(iTotalCount);

        // 没有问题
        if (iTotalCount == 0) {
            System.out.println(typeName + "批次[" + batchName + "]没有问题！");
            return;
        }

        // 得到指定项目的已有的问题
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("type", type);
        mapCondition.put("proj_guid", projectId);
        mapCondition.put("batch_id", batchId);
        int iCurrCount = this.count("d_project_problem", mapCondition);

        // 问题数量一样，说明没有新增变化
        if (iTotalCount == iCurrCount) {
            System.out.println(typeName + "批次[" + batchName + "]没有新增的问题！");
            return;
        }

        System.out.println(typeName + "批次[" + batchName + "]的问题：" + iTotalCount);

        List<Map<String, Object>> listProblems = this.queryNoRec("d_project_problem", mapCondition);
        Map<String, Map<String, Object>> mapProblems = ObjUtil.transListToMap(listProblems, "problem_code");

        int iPageNum = iTotalCount / 100 + 1;
        for (int i = 1; i < iPageNum + 1; i++) {
            // 分页获取具体问题数据
            url = urlData;
            map.put("page", i + "");
            map.put("pageSize", "100");
            result = MockHttpsUtil.doHttpsPost(httpClient, url, map);

            JSONArray jArray = this.parseResult(result);
            // this.printArray(jArray);

            List<Map<String, Object>> listBatchParam = new ArrayList<Map<String, Object>>();
            int iCount = 0;
            for (int h = 0; h < jArray.size(); h++) {
                JSONObject objItem = jArray.getJSONObject(h);

                String probleamCode = objItem.getString("code");

                // 如果当前问题已经存在，则跳过
                if (mapProblems.containsKey(probleamCode)) {
                    continue;
                }

                Map<String, Object> mapBatchParam = new HashMap<String, Object>();
                mapBatchParam.put("room_id", objItem.getString("room_id"));
                mapBatchParam.put("room_name", objItem.getString("roomName"));
                mapBatchParam.put("problem_code", objItem.getString("code"));
                mapBatchParam.put("position_name", objItem.getString("position_name"));
                mapBatchParam.put("problem_descr", objItem.getString("itemName"));
                mapBatchParam.put("contractor_name", objItem.getString("contractorName"));
                mapBatchParam.put("emergency_degree", objItem.getString("emergency_degree"));
                mapBatchParam.put("status", objItem.getString("status"));
                mapBatchParam.put("regist_person", objItem.getString("user_name"));
                mapBatchParam.put("regist_time", objItem.getString("regist_date"));

                listBatchParam.add(mapBatchParam);
                iCount++;

                // todo...遍历，把工地问题入库
                // 批量插入
                if (h == jArray.size() - 1 || iCount >= 70) {
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("proj_guid", projectId);
                    map2.put("batch_id", batchId);
                    map2.put("type", type);
                    map2.put("list", listBatchParam);

                    mockProjectMapper.insertProblemBatch(map2);
                    listBatchParam = new ArrayList<Map<String, Object>>();
                    iCount = 0;
                }

            }

        }

    }

    
    

    /** 抓取交房数据 */
    private void getHandoverRoom(HttpClient httpClient, String projectId, String batchId, String batchName) {

        // 获取该项目的房间
        int iHouseCount = 0;
        Map<String, Object> mapHouseCondition = new HashMap<String, Object>();
        mapHouseCondition.put("project_guid", projectId);
        List<Map<String, Object>> listHouses = projectHousesMapper.listProjectHouses(mapHouseCondition);
        Map<String, Map<String, Object>> mapHouses = ObjUtil.transListToMap(listHouses, "proj_guid");
        iHouseCount = listHouses.size();

        try {

            Map<String, String> map = new HashMap<String, String>();
            map.put("projectId", projectId);
            map.put("batchId", batchId);
            map.put("projectName", "");
            map.put("mode", "search");
            map.put("deliverySituation", "所有房间");
            map.put("deliveryStatus", "已交付");
            map.put("pageSize", "100");
            String url = "https://yf-hb.myyscm.com/sunac/checkroom/delivery/get-deliver-room?_ac=MobileCheckRoom&_fc=DeliverManage&_smp=MobileCheckRoom.DeliverManage";

            int iUpdateCount = 0;
            int iIndex = 1;
            while (iIndex <= (iHouseCount / 100 + 1)) {
                map.put("page", iIndex + "");

                String result = MockHttpsUtil.doHttpsPost(httpClient, url, map);
                // System.out.println(result);

                JSONArray jArray = this.parseResult(result);
                // this.printArrayHandOver(jArray);

                // 如果没有取到数据，则跳出
                if (jArray == null || jArray.size() < 1) {
                    break;
                }

                // todo...遍历，把正式交付的房间数据入库
                for (int h = 0; h < jArray.size(); h++) {
                    JSONObject objItem = jArray.getJSONObject(h);

                    String roomId = objItem.getString("room_id");

                    // 如果当前问题已经存在，则跳过
                    if (mapHouses.containsKey(roomId)) {
                        Map<String, Object> mapHouse = mapHouses.get(roomId);
                        if ("2".equals(StrUtil.getMapValue(mapHouse, "house_status"))) { // 如果状态为 ：2-已交付
                            continue;
                        }
                    }

                    // 更新交付状态和交付时间
                    Map<String, Object> mapCondition = new HashMap<String, Object>();
                    mapCondition.put("proj_guid", roomId);
                    Map<String, Object> mapParam = new HashMap<String, Object>();
                    mapParam.put("house_status", "2");
                    mapParam.put("actual_delivery_time", objItem.getString("delivery_date"));// 实际交付日期
                    this.updateNoRec(mapCondition, "d_project_houses", mapParam);

                    iUpdateCount++;

                }

                iIndex++;
            }

            System.out.println("批次[" + batchName + "]共更新交付房间：" + iUpdateCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    


    private JSONArray parseResult(String result) {
        JSONArray jArray = null;

        if (StrUtil.isNotNull(result)) {
            JSONObject obj = JSON.parseObject(result);

            jArray = obj.parseArray(obj.getString("items"));

        }

        return jArray;
    }

    private void printArray(JSONArray jArray) {

        for (int i = 0; i < jArray.size(); i++) {
            JSONObject objItem = jArray.getJSONObject(i);

            // System.out.print(objItem.getString("id") + "|");
            // System.out.print(objItem.getString("batch_id") + "|");
            System.out.print(objItem.getString("code") + "|");
            System.out.print(objItem.getString("roomName") + "|");
            System.out.print(objItem.getString("itemName") + "|");
            System.out.print(objItem.getString("position_name") + "|");
            System.out.print(objItem.getString("contractorName") + "|");
            System.out.print(objItem.getString("status") + "|");
            System.out.print(objItem.getString("user_name") + "|");
            System.out.print(objItem.getString("regist_date"));
            System.out.println();

        }
    }

    private void printArrayHandOver(JSONArray jArray) {

        for (int i = 0; i < jArray.size(); i++) {
            JSONObject objItem = jArray.getJSONObject(i);

            // System.out.print(objItem.getString("id") + "|");
            // System.out.print(objItem.getString("batch_id") + "|");
            System.out.print(objItem.getString("name") + "|");
            System.out.print(objItem.getString("room_id") + "|");
            System.out.print(objItem.getString("delivery_status") + "|");
            System.out.print(objItem.getString("delivery_situation") + "|");
            System.out.print(objItem.getString("delivery_date") + "|");
            System.out.print(objItem.getString("customer_name") + "|");
            System.out.print(objItem.getString("customer_phone"));
            System.out.println();

        }
    }

}
