package com.cbox.business.timetask.mock.service;

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbox.base.core.service.BaseService;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;
import com.cbox.business.timetask.mock.util.MockUtil;

/** 仅做测试试用，没实际用途。*/
@Service
public class MockHolidayService extends BaseService {

    @Autowired
    private ProjectHousesMapper projectHousesMapper;



    public static void main(String[] args) {
        // MockHolidayService obtainHouses = new MockHolidayService();
        // obtainHouses.getProject();

        String ss = "350302199007020048";
        System.out.println(ss.substring(6, 14));
    }



    /** 获得项目，定时执行，获取第1页数据即可 */
    public void getProject() {

        HttpClient httpClient = MockUtil.mockLogin();

        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;

        try {
            // 获取filter
            dataUrl = "http://api.tianapi.com/txapi/jiejiari/index?key=8ae6c596e229e15a1cc5f6fbb77925c9&type=2&date=2021-09";
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);
            System.out.println(resContent);

            JSONObject jObj = JSON.parseObject(resContent);
            JSONArray jArray = jObj.getJSONArray("newslist");
            for (int i = 0; i < jArray.size(); i++) {
                JSONObject jItem = jArray.getJSONObject(i);
                System.out.println(jItem.get("date") + "," + jItem.get("daycode") + "," + jItem.get("cnweekday") + "," + jItem.get("info"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
