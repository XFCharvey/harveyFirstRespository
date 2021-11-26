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
import com.cbox.business.timetask.mock.util.TimeUtil;

@Service
public class MockCustomerService extends BaseService {

    @Autowired
    private ProjectHousesMapper projectHousesMapper;

    @Autowired
    private MockProjectMapper mockProjectMapper;

    public static void main(String[] args) {
        MockCustomerService obj = new MockCustomerService();
        // String houseGuid = "8ded8d06-9770-4616-af9d-9c439f576557";
        // String houseGuid = "d149f6d0-ed19-40ba-b122-390e2a126b01";
        // String houseId = "";
        //
        // HttpClient httpClient = MockUtil.mockLogin();
        // obj.getCustomer(httpClient, houseGuid, houseId);

        String customerId = "35032119831110235X";
        int iSex = Integer.parseInt(customerId.substring(16, 17));
        System.out.println(iSex);
    }

    /** 定时获取客户及签约信息
     *  项目开始时执行一次，获取所有的历史数据；以后每日定时执行
     * */
    public void callCustomer() {
        HttpClient httpClient = MockUtil.mockLogin();

        // 获取项目数据，按项目查询房间，减少一次性查询的压力
        List<Map<String, Object>> listProject = mockProjectMapper.listProject();

        for (int h = 0; h < listProject.size(); h++) {
            Map<String, Object> mapProject = listProject.get(h);
            String projectId = StrUtil.getMapValue(mapProject, "rec_id");

            // 获取所有未售的房间信息，然后取出房间名称，循环调用
            Map<String, Object> mapHousesCondition = new HashMap<String, Object>();
            mapHousesCondition.put("project_id", projectId);
            List<Map<String, Object>> listHouses = mockProjectMapper.listHousesByStatus(mapHousesCondition);

            if (ObjUtil.isNull(listHouses)) {
                System.out.println("项目【" + StrUtil.getMapValue(mapProject, "project_name") + "】无未售和已交付(无总价)房间信息。");
                continue;
            } else {
                System.out.println("项目【" + StrUtil.getMapValue(mapProject, "project_name") + "】未售和已交付(无总价)房间数量：" + listHouses.size());
            }

            for (int i = 0; i < listHouses.size(); i++) {
                // if (i > 2) {
                // break;
                // }

                Map<String, Object> mapHouse = listHouses.get(i);

                String houseStatus = StrUtil.getMapValue(mapHouse, "house_status");
                String houseName = StrUtil.getMapValue(mapHouse, "house_name");
                String contractTime = StrUtil.getMapValue(mapHouse, "contract_time");
                String houseId = StrUtil.getMapValue(mapHouse, "rec_id");
                String houseGuid = StrUtil.getMapValue(mapHouse, "proj_guid");
                String totalPrice = StrUtil.getMapValue(mapHouse, "total_price");

                // total_price

                if ("2".equals(houseStatus) && StrUtil.isNotNull(totalPrice)) {
                    // 说明有总价信息，并且 当前房子状态是 已交付。则不需要再查询
                    continue;
                }

                System.out.println("houseName：" + houseName);
                this.getCustomer(httpClient, houseGuid, houseId);
            }

            // break;

        }

    }

    public void getCustomer(HttpClient httpClient, String houseGuid, String houseId) {

        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;

        try {

            /* Step1：根据房间号查询基本信息 */
            dataUrl = "http://my.sunac.com.cn:8060/Slxt/XMZB/RoomList_Edit.aspx?mode=2&oid=" + houseGuid + "&funcid=01020204";
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);
            // System.out.println(resContent);

            Map<String, Object> mapValue = MockUtil.getFormData(resContent, "selectBox");
            // MockUtil.printMap(mapValue);

            // 销售状态：key = Status, value = 签约
            // 总价：key = Total, value = 3,938,477.00
            // 产品类型：key = appForm_ProductType, value = 住宅-多层（24米以下/≤8层）-叠拼
            // 装修标准：key = Zxbz, value = 毛坯
            // 实际交付日期：key = BlRhDate, value = 2021-06-15 12:59
            // 缺：付款方式，签约时间，置业顾问
            String houseStatus = StrUtil.getMapValue(mapValue, "Status");
            if (!"签约".equals(houseStatus)) {
                return;
            }
            this.updateHouseInfo(mapValue, houseGuid);

            /* Step2: 获取查询业主的filter串 */
            dataUrl = "http://my.sunac.com.cn:8060/slxt/xmzb/roomlist_edit_cust.aspx?xml=/slxt/xmzb/roomlist_edit_cust_kf.xml&RoomGUID=" + houseGuid + "&funcid=01020204&mode=2";
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);
            // System.out.println(resContent);

            Node node = MockUtil.getNode(resContent, "iframe", 1);
            TagNode tagNode = (TagNode) node;
            String srcStr = tagNode.getAttribute("src");
            String nextFilter = srcStr.substring(srcStr.indexOf("&filter=") + 8, srcStr.indexOf("&processNullFilter="));

            /* Step3: 查询业主信息 */
            dataUrl = "http://my.sunac.com.cn:8060/_grid/griddata.aspx?xml=%2fslxt%2fxmzb%2froomlist_edit_cust_kf.xml&funcid=01020204&gridId=appGrid&sortCol=&sortDir=&vscrollmode=0&multiSelect=0&selectByCheckBox=0&filter="
                    + nextFilter + "&processNullFilter=1&customFilter=&customFilter2=&dependencySQLFilter=&location=&pageNum=1&pageSize=20&showPageCount=1&appName=Default&application=&cp=";
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);
            // System.out.println(resContent);
            listData = MockUtil.getTableData(resContent, "", 0);
            // MockUtil.printList(listData);
            this.updateCustomer(listData, houseId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateHouseInfo(Map<String, Object> mapValue, String houseGuid) {
        // 销售状态：key = Status, value = 签约
        // 总价：key = Total, value = 3,938,477.00
        // 产品类型：key = appForm_ProductType, value = 住宅-多层（24米以下/≤8层）-叠拼
        // 装修标准：key = Zxbz, value = 毛坯
        // 实际交付日期：key = BlRhDate, value = 2021-06-15 12:59
        // 缺：付款方式，签约时间，置业顾问

        Map<String, Object> mapHousesCondition = new HashMap<String, Object>();
        mapHousesCondition.put("proj_guid", houseGuid);
        Map<String, Object> mapHousesParam = new HashMap<String, Object>();
        String totalPrice = StrUtil.getMapValue(mapValue, "Total");
        if (StrUtil.isNotNull(totalPrice)) {
            totalPrice = totalPrice.replace(",", "");
            int iIndex = totalPrice.indexOf(".");
            if (iIndex > 0) {
                totalPrice = totalPrice.substring(0, iIndex);
            }

        }
        mapHousesParam.put("total_price", totalPrice);
        mapHousesParam.put("furbish_type", StrUtil.getMapValue(mapValue, "Zxbz"));
        mapHousesParam.put("house_protype", StrUtil.getMapValue(mapValue, "appForm_ProductType"));
        mapHousesParam.put("rec_updateperson", "sys");

        String actualDeliveryTime = StrUtil.getMapValue(mapValue, "BlRhDate");
        if (StrUtil.isNotNull(actualDeliveryTime)) {
            mapHousesParam.put("actual_delivery_time", actualDeliveryTime);
            mapHousesParam.put("house_status", "2");// 已交付
        } else {
            mapHousesParam.put("house_status", "1");// 签约
        }

        this.update(mapHousesCondition, "d_project_houses", mapHousesParam);
    }

    /** 更新客户信息  */
    public void updateCustomer(List<String[]> listData, String houseId) {

        try {
            /* Step1：查询业主信息 */
            List<Map<String, Object>> listFamily = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < listData.size(); i++) {
                String[] customerArr = listData.get(i);
                // 1|魏剑飞|15676374820 15288388658|350321199104182638|业主

                // TODO：抓取客户信息
                String phoneNo = customerArr[2];
                if (StrUtil.isNull(phoneNo)) {
                    continue;
                }

                if (phoneNo.length() > 11) {
                    phoneNo = phoneNo.split(" ")[0];
                    // 有可能手机号前面多一个0，给过滤掉
                    if (phoneNo.length() > 11) {
                        phoneNo = phoneNo.substring(1);
                    }
                }

                String customerId = customerArr[3];// 身份证号
                if (StrUtil.isNull(customerId)) {
                    continue;
                }
                // 用身份证号来做唯一判断
                Map<String, Object> mapCustomerParam = new HashMap<String, Object>();
                mapCustomerParam.put("customer_id", customerId);
                List<Map<String, Object>> listCurrCustomer = this.queryNoRec("d_customer", mapCustomerParam);

                // 从身份证号中取生日
                String birthdate = "";
                if (StrUtil.isNotNull(customerId)) {
                    birthdate = customerId.substring(6, 14);
                }
                // 从身份证号中取性别
                String cSex = "0";// 默认为男
                if (customerId.length() > 17) {
                    int iSex = Integer.parseInt(customerId.substring(16, 17));
                    if ((iSex % 2) == 0) {
                        cSex = "1";// 偶数是女
                    }
                }

                if (ObjUtil.isNotNull(listCurrCustomer)) {
                    mapCustomerParam = listCurrCustomer.get(0);
                } else {
                    mapCustomerParam = new HashMap<String, Object>();
                    mapCustomerParam.put("customer_name", customerArr[1]);
                    mapCustomerParam.put("customer_sex", cSex);

                    mapCustomerParam.put("customer_id", customerId);// 身份证号
                    mapCustomerParam.put("customer_phone", phoneNo);

                    mapCustomerParam.put("customer_type", "1");
                    mapCustomerParam.put("customer_level", "1");

                    if ("业主".equals(customerArr[4])) {
                        mapCustomerParam.put("customer_status", "8");// 业主
                    } else {
                        mapCustomerParam.put("customer_status", "5");// 准业主
                    }

                    if (StrUtil.isNotNull(birthdate)) {
                        mapCustomerParam.put("customer_birthdate", birthdate);// 客户生日
                    }

                    mapCustomerParam.put("customer_vocation", "qt");

                    TimeUtil.appendRecs(mapCustomerParam);
                    this.saveNoRec("d_customer", mapCustomerParam, true);
                }

                String customerRecId = StrUtil.getMapValue(mapCustomerParam, "rec_id");

                // 插入房源与客户关联表
                mapCustomerParam = new HashMap<String, Object>();
                mapCustomerParam.put("customer_id", customerRecId);
                mapCustomerParam.put("house_id", houseId);
                List<Map<String, Object>> listCustomerHouse = this.queryNoRec("d_customer_houses", mapCustomerParam);
                if (ObjUtil.isNull(listCustomerHouse)) {
                    mapCustomerParam = new HashMap<String, Object>();
                    mapCustomerParam.put("customer_id", customerRecId);
                    mapCustomerParam.put("house_id", houseId);
                    TimeUtil.appendRecs(mapCustomerParam);
                    this.saveNoRec("d_customer_houses", mapCustomerParam, true);
                }

                // 更新房产数量
                mapCustomerParam = new HashMap<String, Object>();
                mapCustomerParam.put("customer_id", customerRecId);
                int iHouseCount = this.count("d_customer_houses", mapCustomerParam);
                if (iHouseCount > 1) {
                    mapCustomerParam = new HashMap<String, Object>();
                    mapCustomerParam.put("customer_level", iHouseCount);
                    Map<String, Object> mapCondition = new HashMap<String, Object>();
                    mapCondition.put("rec_id", customerRecId);
                    this.updateNoRec(mapCondition, "d_customer", mapCustomerParam);
                }

                // 互加为家庭成员
                Map<String, Object> mapFamily = new HashMap<String, Object>();
                mapFamily.put("family_name", customerArr[0]);
                mapFamily.put("family_phone", phoneNo);
                mapFamily.put("family_relations", "亲人");
                if (StrUtil.isNotNull(birthdate)) {
                    mapFamily.put("family_birthday", birthdate);
                }
                mapFamily.put("family_customer_id", customerRecId);
                listFamily.add(mapFamily);
            }


            // 互加为家庭成员。
            if (ObjUtil.isNotNull(listFamily) && listFamily.size() > 1) {
                for (int h = 0; h < listFamily.size(); h++) {
                    Map<String, Object> mapFamily2 = listFamily.get(h);
                    String currCustomerId = StrUtil.getMapValue(mapFamily2, "family_customer_id");
                    for (Map<String, Object> mapFamily : listFamily) {

                        if (currCustomerId.equals(StrUtil.getMapValue(mapFamily, "family_customer_id"))) {
                            continue;
                        }

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("customer_id", currCustomerId);
                        map.put("family_customer_id", StrUtil.getMapValue(mapFamily, "family_customer_id"));
                        List<Map<String, Object>> listCustomerFamily = this.queryNoRec("d_customer_family", map);
                        if (ObjUtil.isNull(listCustomerFamily)) {
                            map = new HashMap<String, Object>();

                            map.put("customer_id", currCustomerId);
                            map.put("family_name", StrUtil.getMapValue(mapFamily, "family_name"));
                            map.put("family_phone", StrUtil.getMapValue(mapFamily, "family_phone"));
                            map.put("family_relations", StrUtil.getMapValue(mapFamily, "family_relations"));
                            map.put("family_birthday", StrUtil.getMapValue(mapFamily, "family_birthday"));
                            map.put("family_customer_id", StrUtil.getMapValue(mapFamily, "family_customer_id"));

                            this.saveNoRec("d_customer_family", map);
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
