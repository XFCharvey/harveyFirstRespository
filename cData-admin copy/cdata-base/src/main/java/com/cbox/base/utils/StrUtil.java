package com.cbox.base.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StrUtil {

    // 默认时间格式
    public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 判断对象是否为空，非空返回false； 空或NULL返回true
     */
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        } else {
            String str = String.valueOf(obj);
            if ("".equals(obj) || "null".equalsIgnoreCase(str) || "undefined".equalsIgnoreCase(str)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static String getStr(Object obj) {
        return getNotNullStrValue(obj);
    }

    public static String getStr(Object obj, String defV) {
        return getNotNullStrValue(obj, defV);
    }

    /**
     * 对象若为空返回,则空字符串。
     * 
     * @param obj
     * @return
     */
    public static String getNotNullStrValue(Object obj) {
        if (obj == null) {
            return "";
        } else {
            Object objValue = obj;
            String value = "";
            if (objValue instanceof java.sql.Timestamp) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
                value = formatter.format((java.sql.Timestamp) objValue);
            } else if (objValue instanceof java.util.Date) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
                value = formatter.format(objValue);
            } else {
                if (isNotNull(objValue)) {
                    value = String.valueOf(objValue);
                }
            }
            return value.trim();
        }
    }

    public static String getNotNullStrValue(Object obj, String defV) {
        if (obj == null) {
            return defV;
        } else {
            return getNotNullStrValue(obj.toString(), defV);
        }
    }

    /**
     * 对字符串为null，则返回空字符串。
     * 
     * @param str 源字符串
     * @return
     */
    public static String getNotNullStrValue(String str) {
        if (str == null || str.equalsIgnoreCase("null") || str.equalsIgnoreCase("undefined")) {
            return "";
        } else {
            return getNotNullStrValue(str, "");
        }
    }

    /**
     * 根据字符串是否为空，若为空的话返回默认字符串。
     * 
     * @param str 源字符串
     * @param defStr 默认字符串
     * @return
     */
    public static String getNotNullStrValue(String str, String defStr) {
        if (StringUtils.isNotEmpty(str) && !str.trim().equals(""))
            return str.trim();
        return defStr;
    }

    /**
     * 根据key获取map值
     */
    @SuppressWarnings("rawtypes")
    public static String getMapValue(Map mapParam, String key, String defaultVal) {
        // key = "<"+key+">";
        String value = "";
        if (mapParam != null && mapParam.get(key) != null) {
            Object objValue = mapParam.get(key);
            if (objValue instanceof java.sql.Timestamp) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
                value = formatter.format((java.sql.Timestamp) objValue);
            } else if (objValue instanceof java.util.Date) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
                value = formatter.format(objValue);
            } else if (objValue instanceof java.math.BigDecimal) {
                value = ((BigDecimal) objValue).stripTrailingZeros().toPlainString();
            } else {
                value = String.valueOf(objValue);
            }
        }

        if (StrUtil.isNull(value) && StrUtil.isNotNull(defaultVal)) {
            value = defaultVal;
        }
        return value;
    }

    @SuppressWarnings("rawtypes")
    public static String getMapValue(Map mapParam, String key) {
        return getMapValue(mapParam, key, "");
    }

    @SuppressWarnings("rawtypes")
    public static int getMapIntValue(Map mapParam, String key) {
        return getMapIntValue(mapParam, key, 0);
    }

    @SuppressWarnings("rawtypes")
    public static int getMapIntValue(Map mapParam, String key, int defaultVal) {
        int value = defaultVal;
        if (mapParam != null && mapParam.get(key) != null) {
            Object objValue = mapParam.get(key);
            if (objValue instanceof Double) {
                value = ((Double) objValue).intValue();
            } else if (objValue instanceof BigDecimal) {
                value = ((BigDecimal) objValue).intValue();
            } else {
                try {
                    value = Integer.parseInt(String.valueOf(objValue));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e);
                }
            }
        }
        return value;
    }

    @SuppressWarnings("rawtypes")
    public static String getMapValueLowerKey(Map mapParam, String key) {
        return getMapValueLowerKey(mapParam, key.toLowerCase(), "");
    }

    @SuppressWarnings("rawtypes")
    public static String getMapValueLowerKey(Map mapParam, String key, String def) {
        return getMapValue(mapParam, key.toLowerCase(), def);
    }

    public static int getNotNullIntValue(String str) {
        int intNum = 0;
        try {
            intNum = Integer.parseInt(str);
        } catch (Exception numForExc) {
            // modify by qiuzq.如果被读取的数是带小数的，则可以避免抛错，直接截取整数部分
            try {
                intNum = (int) Double.parseDouble(str);
            } catch (Exception numForExc1) {
                intNum = 0;
            }
        }
        return intNum;
    }

    /*
     * 特殊字符串转义 html
     */
    public static String filtrateStringToHtml(String str) {
        try {
            if ((str != null) && (!str.trim().equals(""))) {
                str = str.replaceAll("&", "&amp;"); // &号
                str = str.replaceAll("\"", "&quot;"); // "号
                str = str.replaceAll("<", "&lt;"); // 正括号
                str = str.replaceAll(">", "&gt;"); // 反括号
                str = str.replaceAll("\n", "<br>"); // 回车
                str = str.replaceAll(" ", "&nbsp;"); // 空格
                str = str.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"); // TAB键
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * html特殊字符串转义
     */
    public static String filtrateHtmlToString(String str) {
        try {
            if ((str != null) && (!str.trim().equals(""))) {
                str = str.replaceAll("&amp;", "&"); // &号
                str = str.replaceAll("&quot;", "\""); // "号
                str = str.replaceAll("&lt;", "<"); // 正括号
                str = str.replaceAll("&gt;", ">"); // 反括号
                str = str.replaceAll("<br>", "\n"); // 回车
                str = str.replaceAll("&nbsp;", " "); // 空格
                str = str.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\t"); // TAB键
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 字符串编码
     * 
     * @Title: encoderStr
     * @param str
     * @param type
     * @return
     */
    public static String encoderStr(String str, String type) {
        String resultStr = "";
        try {
            resultStr = URLEncoder.encode(str, type);
        } catch (Exception e) {
            System.out.println("字符串加密失败!!");
        }
        return resultStr;
    }

    /**
     * 字符串解码
     * 
     * @Title: decoderStr
     * @param str 源字符串
     * @param type 解码编码
     * @return
     */
    public static String decoderStr(String str, String type) {
        String resultStr = "";
        try {
            resultStr = URLDecoder.decode(str, type);
        } catch (Exception e) {
            System.out.println("字符串解码失败!!");
        }
        return resultStr;
    }

    /** 将捕获的异常详细信息转成字符串 */
    public static String getExceptionTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    /** 只有value不为空时，设置值 */
    public static void setMapValue(Map mapParam, String key, Object value) {
        if (value != null) {
            mapParam.put(key, value);
        }
    }

    /** 只有存在值，才往map赋值
     * @param mapParam 目标对象
     * @param key  
     * @param mapData  数据对象
     * @param keyData  数据对象中的关键字key  
     *  
     *  */
    public static void setMapValue(Map mapParam, String key, Map mapData, String keyData) {
        if (mapData == null || mapData.get(keyData) == null) {

        } else {
            mapParam.put(key, StrUtil.getMapValue(mapData, keyData));
        }
    }

    public static void setMapValue(Map mapParam, String key, Map mapData) {
        String keyData=key;
        setMapValue(mapParam, key, mapData, keyData);
    }

}
