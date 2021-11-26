package com.cbox.common.util;

import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class BaseAspect {

    /**
     * 
     * getFieldsName: 获取字段名和字段值
     *
     * @date: 2018年3月23日 下午3:17:44
     * @author shenliwei
     * @param clazzName
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public Map<String, Object> getFieldsName(String clazzName, String methodName, Object[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(this.getClass());
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                map.put(attr.variableName(i + pos), getRequestParameterMap((HttpServletRequest) args[i]));// paramNames即参数名
            } else {
                map.put(attr.variableName(i + pos), args[i]);// paramNames即参数名
            }
        }
        return map;
    }

    public Object getFieldValue(JoinPoint joinPoint, String fieldName) throws Exception {
        Object returnObject = null;
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();// 获取目标类名
        // String targetName = joinPoint.getTarget().getClass().getSimpleName();// 类名称，不含包名;
        String methodName = ms.getName(); // 获取方法名称
        Object[] args = joinPoint.getArgs();// 参数

        ClassPool pool = ClassPool.getDefault();
        // ClassClassPath classPath = new ClassClassPath(this.getClass());
        ClassClassPath classPath = new ClassClassPath(joinPoint.getTarget().getClass());
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(className);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return returnObject;
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            if (attr.variableName(i + pos).equals(fieldName)) {
                returnObject = args[i];
                break;
            }
        }
        return returnObject;
    }

    /**
     * buildRequestParameterMap:把request参数转换成hashmap
     */
    public static Map<String, String> getRequestParameterMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        // request中的参数转化到map中
        if (request != null) {
            Enumeration<?> e = request.getParameterNames();
            String key = "";
            String value = "";
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.indexOf("[]") > 0) { // 数组类型，把数据从数组中提取出来
                    String[] values = request.getParameterValues(key);
                    String strTemp = "";
                    for (int z = 0; z < values.length; z++) {
                        strTemp += "," + values[z];
                    }
                    value = strTemp.substring(1);
                } else {
                    value = request.getParameter(key);
                }
                params.put(key, value);
                // params.put(key.toUpperCase(), value);
            }
        }
        return params;
    }

}
