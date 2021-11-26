package com.cbox.business.timetask.mock.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.timetask.mock.util.MockUtil;
import com.cbox.business.worktask.worktask.mapper.WorktaskMapper;

/**
 * @ClassName: MockTaskService 
 * @Function: 模拟获取任务工单  
 * 
 * @author qiuzq 
 * @date 2021年9月18日 上午10:49:47 
 * @version 1.0
 */
@Service
public class MockTaskService extends BaseService {

    @Autowired
    private WorktaskMapper worktaskMapper;

    public static void main(String[] args) {
        MockTaskService obtainTaskWork = new MockTaskService();
        obtainTaskWork.getTaskWork();
    }

    private String getUrl(int iPageSize, int iPageNum) {
        // 对应页面查询条件：项目-福建公司，任务归属-全部，任务类型-全部，视图-全部任务
        String dataUrl = "http://my.sunac.com.cn:8060/_grid/griddata.aspx?xml=%2fKfxt%2frcfw%2fRcRwcl_Grid.xml&gridId=appGrid&sortCol=&sortDir=&vscrollmode=0&multiSelect=1&selectByCheckBox=1&filter=i8KtABmW%2b%2bpmpEk4%2b3S3RPD6%2f7P54ZE70B%2bArfqc%2f2rE2RXlcF0%2faI55kCPA%2by60EUJEsZBMQRirN7rTrL4sW2FIBtLAWYmLB0ItuIJczkFexTlsQPIUa6YAgcLomf7Ca7OfG5MMCc5TYVPS3ZWO2zhsGXv6oNISm%2bsTlFdxRJg5e9ONagAFvMYFSYDBQ%2fEjzcZ8V5MUwRlMq5VMEy%2f%2fLcTqqNX2Cn9Ifn19%2bAgmUdZYnL1dL3M3%2bCsUtA0CyUSzLq5XauTXf3hYxxE3g17ADMaf%2biJTiTnZg0AEOueziA4%3d&processNullFilter=1&customFilter=7Mf93klSAzW8XFLqdrGSiLl9j5X4xODJQpo9xBq8EWx0lI33ay7InUruGMlYEmU6qW9vLIZZGK43srzwEnFJzHLo3sSsXz0gaWWAjE4Vc6ovv%2ffaUVxdxEsCa1U%2fnzAYrkKcUHyelGeYlwk9sG8i%2f1QvNvu24G8Ji7D39mP6AK8USeYc5%2bMDdqWUV344LyE6wIsnkQuzGKUgj6AlHK7UEJxzF%2fBe9TIgxQwX1owJkpOAfJIUE4QkQVc%2bcYOiNpRNNVpIG1MkQ6w8qn%2fb3YRiOh1g0SoNQI77tjx3qJQp5soAbRYzW2WQKHw1xLix1eeM99nDVOWg3brTA14JfLgFIO9zrtuLQnN6Bjqtf3riHvg%3d&customFilter2=TTm5zl9l7AYRhO85y0fRIQ%3d%3d&dependencySQLFilter=&location=&showPageCount=1&appName=Default&application=&cp=";
        dataUrl = dataUrl + "&pageNum=" + iPageNum + "&pageSize=" + iPageSize;
        return dataUrl;
    }

    private Map<String, Map<String, Object>> loadUserInfo() {

        Map<String, Object> mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listUser = this.queryNoRec("sys_user", mapParam);
        Map<String, Map<String, Object>> mapUsers = ObjUtil.transListToMap(listUser, "nick_name");

        return mapUsers;
    }

    private Map<String, Map<String, Object>> loadHouses() {

        Map<String, Object> mapParam = new HashMap<String, Object>();
        List<Map<String, Object>> listHouse = this.queryNoRec("d_project_houses", mapParam);
        Map<String, Map<String, Object>> mapHouses = ObjUtil.transListToMap(listHouse, "house_name");

        return mapHouses;
    }


    /** 获得任务，定时执行，获取第1页数据即可 */
    public void getTaskWork() {

        HttpClient httpClient = MockUtil.mockLogin();

        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;

        try {

            Map<String, Map<String, Object>> mapUsers = this.loadUserInfo();
            Map<String, Map<String, Object>> mapHouses = this.loadHouses();

            // 任务工单
            dataUrl = this.getUrl(50, 1);
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);


            listData = MockUtil.getTableData(resContent);
            MockUtil.printList(listData);
            this.dealData(listData, mapUsers, mapHouses);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 获得历史任务，手工执行1次即可 */
    public void getTaskWorkHis() {

        HttpClient httpClient = MockUtil.mockLogin();

        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;

        try {
            Map<String, Map<String, Object>> mapUsers = this.loadUserInfo();
            Map<String, Map<String, Object>> mapHouses = this.loadHouses();
            int iPageNum = 1;
            for (int i = 0; i < 1000; i++) {
                // 最多访问1000页，避免死循环

                dataUrl = this.getUrl(100, iPageNum++);
                resContent = MockUtil.visitUrlGet(dataUrl, httpClient);

                listData = MockUtil.getTableData(resContent);
                if (listData.size() == 0) {
                    break;
                }

                this.dealData(listData, mapUsers, mapHouses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 根据业务规则进行数据处理
     * 性能待完善：一次性获取数据比较，批量更新或插入
     * */
    public void dealData(List<String[]> listData, Map<String, Map<String, Object>> mapUsers, Map<String, Map<String, Object>> mapHouses) {

        for (int i = 0; i < listData.size(); i++) {
            String[] nodeMsg = listData.get(i);

                String taskPeople = "";

                /* 组合生成插入或更新语句 */
                Map<String, Object> mapTaskParam = new HashMap<String, Object>();
                Map<String, Object> mapTaskCondition = new HashMap<String, Object>();

                // 任务编号 5
                mapTaskParam.put("task_code", nodeMsg[5]);
                mapTaskCondition.put("task_code", nodeMsg[5]);

                // 任务类型 6
                String taskType = "repair";
                int iDeadLine = 2;
                if ("维修".equals(nodeMsg[6])) {
                    taskType = "repair";
                    iDeadLine = 3;
                } else if ("投诉".equals(nodeMsg[6])) {
                    taskType = "complain";
                    iDeadLine = 2;
                } else if ("咨询".equals(nodeMsg[6])) {
                    taskType = "consult";
                    iDeadLine = 3;
                }
                mapTaskParam.put("task_type", taskType);

                // 任务主题（名称）7
                mapTaskParam.put("task_name", nodeMsg[7]);
                // 处理对象 8
                String houseName = nodeMsg[8];
                mapTaskParam.put("task_object", houseName);

                if (mapHouses.containsKey(houseName)) {
                    Map<String, Object> map = mapHouses.get(houseName);
                    mapTaskParam.put("project_id", StrUtil.getMapValue(map, "project_id"));
                    mapTaskParam.put("houses_id", StrUtil.getMapValue(map, "rec_id"));
                }

                // 服务请求人 9
                taskPeople += "服务请求人:" + nodeMsg[9];
                // 处理人（受理人）
                String dealPerson = nodeMsg[10];
                taskPeople += ",受理人:" + dealPerson;
                // 最终受理人
                taskPeople += ",最终受理人:" + nodeMsg[11];

                /* 判断当前用户是否存在，如果不存在则新增一个用户作为处理人。手机号怎么办？ */
                if (mapUsers.containsKey(dealPerson)) {
                    Map<String, Object> mapUser = mapUsers.get(dealPerson);
                    mapTaskParam.put("deal_person", StrUtil.getMapValue(mapUser, "user_name"));
                } else {
                    // 如果不存在，则按此名字创建一个用户
                    Map<String, Object> mapUser = new HashMap<String, Object>();
                    mapUser.put("nick_name", dealPerson);
                    mapUser.put("real_name", dealPerson);
                    mapUser.put("user_type", "app"); // app用户
                    mapUser.put("sex", "2");// 未知
                    mapUser.put("identity_type", "1");// 一线员工
                    mapUser.put("status", "0");// 未激活
                    mapUser.put("create_by", "task");
                    mapUser.put("create_time", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
                    this.saveUser("sys_user", mapUser);

                    String userId = StrUtil.getMapValue(mapUser, "user_id");
                    String userName = "RK" + userId;
                    Map<String, Object> mapParam = new HashMap<String, Object>();
                    mapParam.put("user_name", userName);
                    Map<String, Object> mapCondition = new HashMap<String, Object>();
                    mapCondition.put("user_id", userId);
                    this.updateNoRec(mapCondition, "sys_user", mapParam);

                    mapUser.put("user_name", userName);
                    mapUsers.put(dealPerson, mapUser);

                    mapTaskParam.put("deal_person", userName);
                }

                // 任务状态 13
                String taskStatus = "0";
                if ("待派单".equals(nodeMsg[13])) {
                    taskStatus = "0";
                } else if ("待联系".equals(nodeMsg[13])) {
                    taskStatus = "1";
                } else if ("实施中".equals(nodeMsg[13])) {
                    taskStatus = "2";
                } else if ("已关闭".equals(nodeMsg[13])) {
                    taskStatus = "3";
                    mapTaskParam.put("finish_rate", 100);
                    mapTaskParam.put("finish_time", DateUtils.dateTimeNow());
                } else if ("已完成".equals(nodeMsg[13])) {
                    taskStatus = "4";
                    mapTaskParam.put("finish_rate", 100);
                    mapTaskParam.put("finish_time", DateUtils.dateTimeNow());
                }
                mapTaskParam.put("task_status", taskStatus);
                // 任务描述
                mapTaskParam.put("task_detail", nodeMsg[16]);
                mapTaskParam.put("deal_people", taskPeople);
                
                Map<String, Object> mapWorkTask = worktaskMapper.getWorktaskByCode(mapTaskCondition);

                // 设置超时时间
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, iDeadLine);
                Date newDate = calendar.getTime();
                mapTaskParam.put("task_deadline", DateUtils.format(newDate, DateUtils.YYYY_MM_DD_HH_MM_SS));

                if (ObjUtil.isNull(mapWorkTask)) {
                    // // 受理时间
                    mapTaskParam.put("rec_person", "task");
                    mapTaskParam.put("rec_updateperson", "task");
                    // 保存新增
                    this.save("d_worktask", mapTaskParam);
                } else {
                    String taskStatus2 = StrUtil.getMapValue(mapWorkTask, "task_status");
                    if ("4".equals(taskStatus2)) {
                        System.out.println("该任务在库表状态为已结束，无需更新操作");
                    } else {
                        String finishTime = StrUtil.getMapValue(mapWorkTask, "finish_time");
                        if (StrUtil.isNotNull(finishTime)) {
                            // 如果已有完成期限，则不更新
                            mapTaskParam.remove("finish_time");
                        }

                        // 更新
                        mapTaskParam.put("rec_updateperson", "task");
                        this.update(mapTaskCondition, "d_worktask", mapTaskParam);
                    }
                }
            }

    }
}
