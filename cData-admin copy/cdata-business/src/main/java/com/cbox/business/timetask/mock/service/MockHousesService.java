package com.cbox.business.timetask.mock.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;
import com.cbox.business.timetask.mock.mapper.MockProjectMapper;
import com.cbox.business.timetask.mock.util.MockUtil;

@Service
public class MockHousesService extends BaseService {

    @Autowired
    private ProjectHousesMapper projectHousesMapper;

    @Autowired
    private MockProjectMapper mockProjectMapper;

    public static void main(String[] args) {
        MockHousesService obtainHouses = new MockHousesService();
        obtainHouses.callHouses();
    }

    private String getUrl(int iPageSize, int iPageNum, String filterStr) {
        // 基础资料->房间管理
        String dataUrl = "http://my.sunac.com.cn:8060/_grid/griddata.aspx?xml=%2fSlxt%2fXMZB%2fRoomList_Grid_Kf.xml&funcid=01020204&gridId=appGrid&sortCol=&sortDir=&vscrollmode=0&multiSelect=1&selectByCheckBox=0&processNullFilter=1&customFilter=&customFilter2=&dependencySQLFilter=&location=&showPageCount=1&appName=Default&application=&cp="
                + "&" + filterStr;
        dataUrl = dataUrl + "&pageNum=" + iPageNum + "&pageSize=" + iPageSize;
        return dataUrl;
    }

    /** 获取房间数据 
     *  项目初始执行一次，后面每日定时执行一次
     * */
    public void callHouses() {
        HttpClient httpClient = MockUtil.mockLogin();

        // TODO:
        // step1：获取所有的项目，遍历
        List<Map<String, Object>> listProject = mockProjectMapper.listProject();
        Map<String, Map<String, Object>> mapProjects = ObjUtil.transListToMap(listProject, "proj_guid");

        for (int i = 0; i < listProject.size(); i++) {
            Map<String, Object> mapProject = listProject.get(i);
            String projGuid = StrUtil.getMapValue(mapProject, "proj_guid");

            // step2：统计该项目已有的房间数
            int iHouseCount = 0;
            Map<String, Object> mapHouseCondition = new HashMap<String, Object>();
            mapHouseCondition.put("project_guid", projGuid);
            List<Map<String, Object>> listHouses = projectHousesMapper.listProjectHouses(mapHouseCondition);
            Map<String, Map<String, Object>> mapHouses = ObjUtil.transListToMap(listHouses, "proj_guid");
            iHouseCount = listHouses.size();

            this.getHouses(httpClient, projGuid, iHouseCount, mapHouses, mapProjects);

            // 先只跑一个项目
            // break;
        }

    }

    /** 获得房源 */
    public void getHouses(HttpClient httpClient, String projGuid, int iHouseCount, Map<String, Map<String, Object>> mapHouses, Map<String, Map<String, Object>> mapProjects) {

        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;

        try {
            // 获取filter
            dataUrl = "http://my.sunac.com.cn:8060/Slxt/XMZB/RoomList_Grid.aspx?xml=%2FSlxt%2FXMZB%2FRoomList_Grid_Kf.xml&funcid=01020204&filter=%3Centity%20name%3D%22vp_RoomList%22%20primarykey%3D%22RoomGUID%22%3E%3Cfilter%20type%3D%22and%22%3E%3Cfilter%20type%3D%22and%22%2F%3E%3Cfilter%20type%3D%22and%22%3E%3Ccondition%20operator%3D%22in%22%20attribute%3D%22ProjGUID%22%20value%3D%22"
                    + projGuid + "%22%2F%3E%3C%2Ffilter%3E%0D%0A%3C%2Ffilter%3E%3C%2Fentity%3E";
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);
            // System.out.println(resContent);
            Node node = MockUtil.getNode(resContent, "iframe", 1);
            TagNode tagNode = (TagNode) node;

            String srcStr = tagNode.getAttribute("src");
            String filterStr = "";
            if (StrUtil.isNotNull(srcStr)) {
                String[] srcs = srcStr.split("&");
                for (int i = 0; i < srcs.length; i++) {
                    if (srcs[i].contains("filter")) {
                        filterStr = srcs[i];
                    }
                }
            }

            // 获取房源数据
            dataUrl = this.getUrl(100, 1, filterStr);
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);

            Map<String, Object> mapProject = mapProjects.get(projGuid);

            node = MockUtil.getNode(resContent, "table", 0);
            tagNode = (TagNode) node;
            String rowcount = tagNode.getAttribute("rowcount");
            boolean bDone = true;
            if (StrUtil.isNotNull(rowcount)) {
                if (iHouseCount >= Integer.parseInt(rowcount)) {
                    // 房间数一样，无需重新获取房源
                    bDone = false;
                    System.out.println(StrUtil.getMapValue(mapProject, "project_name") + "：当前项目无新房源。");
                }
            }

            if (bDone) {
                this.doGetHouse(httpClient, filterStr, Integer.parseInt(rowcount), resContent, projGuid, mapHouses, mapProjects);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doGetHouse(HttpClient httpClient, String filterStr, int iHouseCount, String firstResContent, String projGuid, Map<String, Map<String, Object>> mapHouses,
            Map<String, Map<String, Object>> mapProjects) {
        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;
        
        Map<String, Object> mapProject = mapProjects.get(projGuid);

        try {
            int iIndex = 1;
            while (iIndex <= (iHouseCount / 100 + 1)) {
                // 获取房源数据，获取所有的分页
                if (iIndex == 1) {
                    resContent = firstResContent;
                } else {
                    dataUrl = this.getUrl(100, iIndex, filterStr);
                    resContent = MockUtil.visitUrlGet(dataUrl, httpClient);
                }

                System.out.println("项目：" + StrUtil.getMapValue(mapProject, "project_name") + ",Total:" + (iHouseCount / 100 + 1) + ",Page" + iIndex);
                listData = MockUtil.getTableData(resContent, "oid", 0);
                // MockUtil.printList(listData);

                List<Map<String, Object>> listHousesParam = new ArrayList<Map<String, Object>>();

                int iCount = 0;
                // TODO: 房间数据入库
                for (int i = 0; i < listData.size(); i++) {
                    String[] housesArr = listData.get(i);
                    String houseGuid = housesArr[0];

                    
                    if (mapHouses.containsKey(houseGuid)) {
                        // 如果是最后一个，提交一次，避免丢失
                        if (i == listData.size() - 1) {
                            if (ObjUtil.isNotNull(listHousesParam)) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("project_id", StrUtil.getMapValue(mapProject, "rec_id"));
                                map.put("project_guid", projGuid);
                                map.put("list", listHousesParam);

                                mockProjectMapper.insertHousesBatch(map);
                            }
                        }

                        continue;
                    }
                    


                    Map<String, Object> mapHousesParam = new HashMap<String, Object>();

                    mapHousesParam.put("proj_guid", houseGuid);

                    mapHousesParam.put("house_name", housesArr[2]);
                    mapHousesParam.put("house_type", housesArr[3]);
                    mapHousesParam.put("house_area", housesArr[6]);
                    mapHousesParam.put("house_buildarea", housesArr[5]);


                    listHousesParam.add(mapHousesParam);

                    iCount++;

                    // 批量插入
                    if (i == listData.size() - 1 || iCount >= 70) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("project_id", StrUtil.getMapValue(mapProject, "rec_id"));
                        map.put("project_guid", projGuid);
                        map.put("rec_person", "admin");
                        map.put("rec_updateperson", "admin");
                        map.put("list", listHousesParam);

                        mockProjectMapper.insertHousesBatch(map);
                        listHousesParam = new ArrayList<Map<String, Object>>();
                        iCount = 0;
                    }
                }

                iIndex++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
