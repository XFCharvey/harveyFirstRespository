package com.cbox.base.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: ObjUtil
 * @Function: 对象的工具集
 * 
 * @author cbox
 * @date 2020年3月16日 上午8:28:02
 * @version 1.0
 */
public class ObjUtil {

    /** 判断list是否为空 */
    public static boolean isNull(List list) {
        if (list == null || list.isEmpty()) {
            // 用isEmpty，数据量大，效率高
            return true;
        } else {
            return false;
        }
    }

    /** 判断list是否不为空 */
    public static boolean isNotNull(List list) {
        return !isNull(list);
    }

    /** 判断map是否为空 */
    public static boolean isNull(Map map) {
        if (map == null || map.isEmpty()) {
            // 用isEmpty，数据量大，效率高
            return true;
        } else {
            return false;
        }
    }

    /** 判断map是否不为空 */
    public static boolean isNotNull(Map map) {
        return !isNull(map);
    }

    /** 把List转化为指定key的map结构返回，要求key是唯一键，不然会覆盖 */
    public static <T> Map<String, T> listToMap(List<T> list, String key) {
        Map<String, T> destMap = new LinkedHashMap<String, T>();

        try {
            if (isNotNull(list)) {
                for (T object : list) {
                    PropertyDescriptor pd = new PropertyDescriptor(key, object.getClass());
                    Object keyValue = pd.getReadMethod().invoke(object);
                    destMap.put(StrUtil.getStr(keyValue), object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return destMap;
    }
    
    /** 把List转化为指定key的map结构返回，要求key是唯一键，不然会覆盖 */
    public static <T> Map<String, Map<String, T>> transListToMap(List<Map<String, T>> list, String key) {
        Map<String, Map<String, T>> map = new LinkedHashMap<String, Map<String, T>>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, T> tmpMap = list.get(i);
            map.put(StrUtil.getMapValue(tmpMap, key), tmpMap);
        }

        return map;
    }
    
    /** 把List转化为指定key的map结构返回，map的value存放的还是List*/
    public static <T> Map<String, List<Map<String, T>>> transList(List<Map<String, T>> list, String key) {
        Map<String, List<Map<String, T>>> mapReturn = new LinkedHashMap<String, List<Map<String, T>>>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, T> tmpMap = list.get(i);
            
            List<Map<String, T>> listTmp=null;
            
            String newKey=StrUtil.getMapValue(tmpMap, key);
            if(mapReturn.containsKey(newKey)) {
                listTmp=mapReturn.get(newKey);
            }else {
                listTmp=new ArrayList<Map<String, T>>();
            }
            
            listTmp.add(tmpMap);
            
            mapReturn.put(newKey, listTmp);
        }

        return mapReturn;
    }

    /** 转化Map中的key为List返回*/
    public static <T> List<T> mapToListByValue(Map<String, T> map) {
        List<T> list;

        if (isNotNull(map)) {
            list = new ArrayList<T>(map.values());
        } else {
            list = new ArrayList<T>();
        }

        return list;
    }

    /** 转化Map中的value为List返回*/
    public static <T> List<String> mapToListByKey(Map<String, T> map) {
        List<String> list;

        if (isNotNull(map)) {
            list = new ArrayList<String>(map.keySet());
        } else {
            list = new ArrayList<String>();
        }

        return list;
    }

   

    /**
          * 根据map中的某个key 去除List中重复的map
     * @author hfx
     * @param list
     * @param mapKey
     * @return 去重后的list
     */
    public static List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>> list, String mapKey) {

        // 把list中的数据转换成msp,去掉同一key值多余数据，保留查找到第一个key值对应的数据
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Map<String, Object>> msp = new HashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map<String, Object> map = list.get(i);
            String keyValue = StrUtil.getMapValue(map, mapKey);
            map.remove(mapKey);
            msp.put(keyValue, map);
        }
        // 把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
        Set<String> mspKey = msp.keySet();
        for (String key : mspKey) {
            Map<String, Object> newMap = msp.get(key);
            newMap.put(mapKey, key);
            listMap.add(newMap);
        }
        return listMap;
    }
}
