package com.cbox.business.timetask.mock.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Maps;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;
import com.cbox.business.timetask.mock.mapper.MockProjectMapper;
import com.cbox.business.timetask.mock.util.MockUtil;

@Service
public class MockProjectService extends BaseService {

    @Autowired
    private ProjectHousesMapper projectHousesMapper;

    @Autowired
    private MockProjectMapper mockProjectMapper;


    public static void main(String[] args) {
        MockProjectService obtainHouses = new MockProjectService();
        obtainHouses.getProject();
    }

    private String getUrl(int iPageSize, int iPageNum) {
        // 基础资料->项目概况
        String dataUrl = "http://my.sunac.com.cn:8060/PubProject/Project/Project.aspx?funcid=01020202&xml=%2FPubProject%2FProject%2FProject_KF.xml";
        // dataUrl = dataUrl + "&pageNum=" + iPageNum + "&pageSize=" + iPageSize;
        return dataUrl;
    }

    /** 获得项目，定时执行，获取第1页数据即可 */
    public void getProject() {
        String nowTime = DateUtils.getTime();
        HttpClient httpClient = MockUtil.mockLogin();
        // HttpClient httpClient = new HttpClient();

        String dataUrl = "";
        String resContent = "";
        List<String[]> listData;

        try {
            // 区域公司设置
            Map<String, String> params = Maps.newHashMap();
            params.put("BUGUID", "8fdac8b0-498d-432c-abdc-f4213819d8ad");
            params.put("BUName", "%E7%A6%8F%E5%BB%BA%E5%85%AC%E5%8F%B8");// url编码， 福建公司
            params.put("IsEndCompany", "1");
            String url = "http://my.sunac.com.cn:8060/Desktop/FunctionSet/Function_XmlHttp.aspx?action=UserLogin&isLogin=true";
            String aaa = MockUtil.visitUrlPost(url, params, httpClient);


            dataUrl = this.getUrl(100, 1);
            resContent = MockUtil.visitUrlGet(dataUrl, httpClient);

            listData = MockUtil.getTableData(resContent, "oid,ProjCode", 6);
            MockUtil.printList(listData);
            // this.dealData(listData);

            List<String[]> listProject = new ArrayList<String[]>();
            List<String[]> listGroup = new ArrayList<String[]>();

            String proId = "";
            String lastName = "";
            // 跳过前面两条，分拆数据
            for (int i = 2; i < listData.size(); i++) {
                String[] newItems = new String[3];

                String[] items = listData.get(i);
                String currProId = items[1];

                newItems[0] = items[0];
                String name = items[2].replace("　", "");
                boolean bSubPro = false;
                if (StrUtil.isNotNull(proId) && currProId.startsWith(proId)) {
                    // 子项目
                    if (!name.contains(lastName)) {
                        name = lastName + name;
                    }
                    bSubPro = true;
                } else {
                    // 项目分组
                    proId = currProId;
                    name = name.substring(name.indexOf("]") + 1);
                    lastName = name;
                }
                newItems[1] = proId;
                newItems[2] = name;

                if (bSubPro) {
                    listProject.add(newItems);
                } else {
                    listGroup.add(newItems);
                }
            }


            System.out.println("Group:" + listGroup.size());
            MockUtil.printList(listGroup);
            System.out.println("Project:" + listProject.size());
            MockUtil.printList(listProject);
            // TODO: 把listGroup的数据入d_project_group表。只插入不更新，proj_guid唯一
            for (int i = 0; i < listGroup.size(); i++) {
                Map<String, Object> mapProGroupParam = new HashMap<String, Object>();
                String[] mapGroup = listGroup.get(i);
                mapProGroupParam.put("proj_guid", mapGroup[0]);

                Map<String, Object> mapGroupData = mockProjectMapper.getGroupByGuid(mapProGroupParam);

                if (ObjUtil.isNull(mapGroupData)) {
                    mapProGroupParam.put("group_id", mapGroup[1]);
                    mapProGroupParam.put("group_name", mapGroup[2]);
                    mapProGroupParam.put("rec_person", "admin");
                    mapProGroupParam.put("rec_updateperson", "admin");

                    this.save("d_project_group", mapProGroupParam);
                }

                // System.out.println(mapGroup[2]);
            }
            // TODO: 把listProject的数据入d_project表。只插入不更新，proj_guid唯一

            for (int i = 0; i < listProject.size(); i++) {
                Map<String, Object> mapProjectParam = new HashMap<String, Object>();
                String[] mapProject = listProject.get(i);
                mapProjectParam.put("proj_guid", mapProject[0]);

                Map<String, Object> mapProjectData = mockProjectMapper.getProjectByGuid(mapProjectParam);

                if (ObjUtil.isNull(mapProjectData)) {
                    mapProjectParam.put("pro_groupid", mapProject[1]);
                    mapProjectParam.put("project_name", mapProject[2]);
                    mapProjectParam.put("rec_person", "admin");
                    mapProjectParam.put("rec_updateperson", "admin");
                    this.save("d_project", mapProjectParam);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
