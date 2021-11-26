package com.cbox.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.common.bean.BaseTreeVO;

public class BaseTreeUtil {

    private static int size = 50;

    /**
     * 操作树：BaseTree数据转换
     * 
     * @param list tree列表
     * @return tree节点数据
     */
    public static JSONArray toJsonString(List<BaseTreeVO> list) {

        if (list != null) {
            boolean isDefaultOpen = list.size() < size ? true : false;
            JSONArray childs = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                BaseTreeVO baseTreeVO = list.get(i);
                JSONObject child = new JSONObject();
                child.put("id", baseTreeVO.getId());
                String pId = baseTreeVO.getPid();
                child.put("pId", pId);
                String code = StrUtil.getNotNullStrValue(baseTreeVO.getCode(), baseTreeVO.getId());
                child.put("code", code);
                child.put("name", baseTreeVO.getName());
                child.put("isParent", baseTreeVO.isParent());
                child.put("title", baseTreeVO.getName());// 节点提示信息

                child.put("open", isDefaultOpen || baseTreeVO.isOpen());
                child.put("checked", baseTreeVO.isChecked());
                child.put("nocheck", baseTreeVO.isNocheck());
                child.put("noEditBtn", baseTreeVO.isNoEditBtn());
                child.put("noRemoveBtn", baseTreeVO.isNoRemoveBtn());

                // 自定义业务参数
                child.put("diy1", baseTreeVO.getDiy1());
                child.put("diy2", baseTreeVO.getDiy2());
                child.put("diy3", baseTreeVO.getDiy3());
                child.put("diy4", baseTreeVO.getDiy4());
                childs.add(child);
            }
            return childs;
        }
        return null;
    }

    /**
     * 操作树：BaseTree数据转换(全局指定树上按钮)
     * 
     * @param list tree列表
     * @return tree节点数据
     */
    /**
     * BaseTree数据转换(全局指定树上按钮)
     * 
     * @param list
     * @param hasCheck
     * @param hasEditBtn
     * @param hasRemoveBtn
     * @return
     */
    public static JSONArray toJsonString(List<BaseTreeVO> list, boolean hasCheck, boolean hasEditBtn, boolean hasRemoveBtn) {

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                BaseTreeVO baseTreeVO = list.get(i);

                // 全局指定树上的按钮
                baseTreeVO.setNocheck(hasCheck);
                baseTreeVO.setNoEditBtn(hasEditBtn);
                baseTreeVO.setNoRemoveBtn(hasRemoveBtn);
            }
        }

        return toJsonString(list);
    }

    /**
     * toNodesForZTree:数据字典：转换成 ztree 的返回数据结构
     *
     * @date: 2019年10月22日 下午4:09:39
     * @author cbox
     * @param list
     * @return
     */
    public static String toNodesForZTree(List<BaseTreeVO> list) {
        if (list != null) {
            boolean isDefaultOpen = list.size() < size ? true : false;
            JSONArray childs = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                BaseTreeVO baseTreeVO = list.get(i);
                // System.out.println(baseTreeVO.isOpen());
                JSONObject child = new JSONObject();
                child.put("id", baseTreeVO.getId());
                String pId = baseTreeVO.getPid();
                child.put("pId", pId);
                String code = StrUtil.getNotNullStrValue(baseTreeVO.getCode(), baseTreeVO.getId());
                child.put("code", code);
                child.put("name", baseTreeVO.getName());
                child.put("isParent", baseTreeVO.isParent());
                child.put("title", baseTreeVO.getName());// 节点提示信息

                child.put("open", isDefaultOpen || baseTreeVO.isOpen());
                child.put("checked", baseTreeVO.isChecked());
                child.put("nocheck", baseTreeVO.isNocheck());

                // is_allparams diy1,req_params diy2,rsp diy3
                child.put("is_allparams", baseTreeVO.getDiy1());
                child.put("req_params", baseTreeVO.getDiy2());
                child.put("rsp", baseTreeVO.getDiy3());
                childs.add(child);
            }
            return childs.toString();
        }
        return null;
    }

    /**
     * toNodesForDTree:数据字典：转换成 dtree 的返回数据结构
     *
     * @date: 2019年10月22日 下午4:09:39
     * @author cbox
     * @param list
     * @return
     */
    public static List<Map<String, Object>> toNodesForDTree(List<BaseTreeVO> list) {

        List<Map<String, Object>> listTreeData = new ArrayList<Map<String, Object>>();

        boolean bFirstTop = true;

        boolean isDefaultOpen = list.size() < size ? true : false;
        for (int i = 0; i < list.size(); i++) {
            BaseTreeVO baseTreeVO = list.get(i);

            Map<String, Object> mapTree = new HashMap<String, Object>();

            mapTree.put("id", baseTreeVO.getId());
            String pId = baseTreeVO.getPid();
            pId = StrUtil.getNotNullStrValue(pId, "0");// 顶层节点默认为0
            mapTree.put("parentId", pId);
            mapTree.put("title", baseTreeVO.getName());// 节点提示信息

            if ("0".equals(pId) && bFirstTop) {// 第一个顶部节点，给自动展开
                baseTreeVO.setOpen(true);
                bFirstTop = false;
            }

            mapTree.put("spread", isDefaultOpen || baseTreeVO.isOpen());// 节点展开状态

            mapTree.put("checkArr", baseTreeVO.isChecked() ? "1" : "0");// 支持复选

            // 自定义返回数据
            JSONObject childCustom = new JSONObject();
            String code = StrUtil.getNotNullStrValue(baseTreeVO.getCode(), baseTreeVO.getId());
            childCustom.put("code", code);// 传递的编码，可以是多个的组合
            String title = StrUtil.getNotNullStrValue(baseTreeVO.getTitle(), baseTreeVO.getName());
            childCustom.put("title", title);// 展示的长名称

            mapTree.put("basicData", childCustom);

            listTreeData.add(mapTree);
        }

        return listTreeData;

    }
    
    
    /**
     * toNodesForVueTree:数据字典：转换成vue tree 的返回数据结构(id,value,label,children)
     * @param list 列表数据
     * @param id  指定id的key
     * @param pId 指定父id的key
     * @param name  指定名称的key
     * @param value  指定value的key（有些组件使用到value）
     * @return
     */
    public static List<Map<String, Object>> toNodesForVueTree(List<Map<String, Object>> list, String id, String pId, String value, String name, Boolean checked) {

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        List<String> tempList = new ArrayList<String>();
        for (Map<String, Object> map : list){
            tempList.add(StrUtil.getMapValue(map,id));
            map.put("id", StrUtil.getMapValue(map, id));
            map.put("label", StrUtil.getMapValue(map, name));
            map.put("pId", StrUtil.getMapValue(map, pId));
            map.put("value", StrUtil.getMapValue(map, value));
            map.put("checked", checked);
        }
        for (Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext();){
        	Map<String, Object> map = (Map<String, Object>) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(StrUtil.getMapValue(map, pId))){
                recursionFn(list, map,id,pId);
                returnList.add(map);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = list;
        }
        return returnList;
    }
    
    /**
     * toNodesForVueTreeFilter:带筛选过滤的数据转义
     *
     * @date: 2021年2月4日 上午9:57:29 
     * @author qiuzq 
     * @param list
     * @param id
     * @param pId
     * @param value
     * @param name
     * @param bFilter 是否要筛选
     * @param filterId  筛选的id值
     * @param allPIdColumn 指定存放所有父节点的字段，如为空默认为 ancestors
     * @return
     */
    public static List<Map<String, Object>> toNodesForVueTreeFilter(List<Map<String, Object>> list, String id, String pId, String value, String name, boolean checked, boolean bFilter, String filterId,
            String allPIdColumn) {

        if (bFilter) {
            if (StrUtil.isNull(allPIdColumn)) {
                allPIdColumn = "ancestors";
            }

            // 过滤节点及其字节点数据
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = it.next();
                if (StrUtil.getMapValue(map, id).equals(filterId) || ArrayUtils.contains(StringUtils.split(StrUtil.getMapValue(map, allPIdColumn), ","), filterId)) {
                    it.remove();
                }
            }
        }

        return toNodesForVueTree(list, id, pId, value, name, checked);
    }
    
    /**
     * 递归列表
     */
    private static void recursionFn(List<Map<String, Object>> list, Map<String, Object> map,String id,String pId)
    {
        // 得到子节点列表
        List<Map<String, Object>> childList = getChildList(list, map,id,pId);
        System.out.println(childList);
        if (childList.size() > 0) {
            map.put("children", childList);
        }
        for (Map<String, Object> mapChild : childList)
        {
            if (hasChild(list, mapChild,id,pId))
            {
                recursionFn(list, mapChild,id,pId);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<Map<String, Object>> getChildList(List<Map<String, Object>> list, Map<String, Object> map,String id,String pId)
    {
        List<Map<String, Object>> tlist = new ArrayList<Map<String, Object>>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext())
        {
        	Map<String, Object> n = (Map<String, Object>) it.next();
            if (StrUtil.getMapValue(map, id).equals(StrUtil.getMapValue(n, pId)))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<Map<String, Object>> list, Map<String, Object> map,String id,String pId)
    {
        return getChildList(list, map,id,pId).size() > 0 ? true : false;
    }

}
