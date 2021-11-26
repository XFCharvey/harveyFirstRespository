package com.cbox.base.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CboxUtils {

    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(Object o) {
        return o != null && !"".equals(o);
    }

    public static boolean isEmptyMap(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmptySet(Set set) {
        return set == null || set.isEmpty();
    }

    public static boolean requiredParams(Map inMap, Set<String> requiredParas) {
        if (inMap == null || inMap.isEmpty()) {
            return true;
        }
        if (inMap.size() < requiredParas.size()) {
            return true;
        }
        for (String requiredPara : requiredParas) {
            if (StringUtils.isEmpty((String) inMap.get(requiredPara))) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkLen(Map inMap, Set<String> params, Set<String> paramLen) {
        if (inMap == null || inMap.isEmpty()) {
            return false;
        }
        if (isEmptySet(params) || isEmptySet(paramLen)) {
            return false;
        }
        List<String> pList = Lists.newArrayList(params);
        List<String> lList = Lists.newArrayList(paramLen);
        for (int i = 0; i < Math.min(pList.size(), lList.size()); i++) {
            String o = (String) inMap.get(pList.get(i));
            String pl = lList.get(i);
            long len = Long.parseLong(pl.substring(1));
            switch (pl.charAt(0)) {
            case 'F':
                if (StringUtils.isEmpty(o) || o.length() != len) {
                    return true;
                }
            case 'V':
                if (StringUtils.isNotEmpty(o) && o.length() > len) {
                    return true;
                }
            default:
                break;
            }
        }
        return false;
    }

    public static void copyMap(Map outMap, Map inMap, String keys) {
        copyMap(outMap, inMap, keys.split(","));
    }
    public static void copyMap(Map outMap, Map inMap, String[] keys) {
        Map<String, String> keyMap = Maps.newHashMap();
        for (String key : keys) {
            keyMap.put(key, key);
        }
        copyMap(outMap, inMap, keyMap);
    }
    
    public static void copyMap(Map outMap, Map inMap, List<String> keys) {
        Map<String, String> keyMap = Maps.newHashMap();
        for (String key : keys) {
            keyMap.put(key, key);
        }
        copyMap(outMap, inMap, keyMap);
    }

    public static void copyMap(Map outMap, Map inMap, Map<String, String> keyMap) {
        keyMap.forEach((key, value) -> {
            outMap.put(key, inMap.get(value));
        });
    }

    public static <T> Map<T, T> toMap(T[][] array) {
        if (array == null) {
            return Maps.newHashMap();
        }
        Map<T, T> map = Maps.newHashMapWithExpectedSize(array.length);
        for (int i = 0; i < array.length; ++i) {
            T[] object = array[i];
            if (object.length != 2) {
                throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length 2");
            }
            map.put(object[0], object[1]);
        }
        return map;
    }

    public static Map<String, List<Map>> listGroupByKey(List<Map> dataList, String key) {
        return listGroupByKey(dataList, key, null);
    }

    public static Map<String, List<Map>> listGroupByKey(List<Map> dataList, String key, List<String> deleteKeys) {
        Map<String, List<Map>> dataMap = Maps.newHashMap();
        for (Map data : dataList) {
            String groupKey = String.valueOf(data.getOrDefault(key, ""));
            if (StringUtils.isEmpty(groupKey) || "null".equalsIgnoreCase(groupKey)) {
                groupKey = "";
            }
            List<Map> stateList = dataMap.getOrDefault(groupKey, Lists.newArrayList());
            if (!isEmptyList(deleteKeys)) {
                for (String deleteKey : deleteKeys) {
                    data.remove(deleteKey);
                }
            }
            stateList.add(data);
            dataMap.put(groupKey, stateList);
        }
        return dataMap;
    }

    public static Map<Object, Map> convertListToMap(String primaryKey, List<Map> dataList) {
        return convertListToMap(primaryKey, dataList, null);
    }

    public static Map<Object, Map> convertListToMap(String primaryKey, List<Map> dataList, List<String> deleteKeys) {
        if (isEmptyList(dataList)) {
            return Maps.newHashMap();
        }
        Map<Object, Map> result = Maps.newHashMap();
        for (Map map : dataList) {
            result.put(map.get(primaryKey), map);
            if (isEmptyList(deleteKeys)) {
                continue;
            }
            for (String deleteKey : deleteKeys) {
                map.remove(deleteKey);
            }
        }
        return result;
    }

    public static String getKey(Object key, Object interfaceName, Map<String, Map<String, String>> callback) {
        if (isEmptyMap(callback)) {
            return (String) key;
        }
        Map<String, String> mapper = callback.get(interfaceName);
        if (isEmptyMap(mapper)) {
            return (String) key;
        }
        return mapper.getOrDefault(key, (String) key);
    }

    // 移除map中的空白字符串值
    public static void removeEmptyKey(Map<String, String> param) {
        Iterator<String> iter = param.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (com.cbox.base.utils.StringUtils.isEmpty(param.get(key))) {
                // param.remove(key); // java.util.ConcurrentModificationException
                iter.remove(); // OK
            }
        }
    }
}
