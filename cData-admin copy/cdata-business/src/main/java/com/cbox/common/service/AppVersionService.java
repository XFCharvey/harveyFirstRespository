package com.cbox.common.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.common.mapper.AppMapper;
import com.google.common.collect.Maps;

@Service
@Transactional
public class AppVersionService extends BaseService {

    private String tableName = "s_app_version";

    @Autowired
    private AppMapper appMapper;

    /**
     * //版本检查
     * 
     * @param appid
     * @param version 客户端请求的版本号，
     * @param os 客户端系统名称：Android、iOS
     * @param force 是否是强制升级版本
     * @return
     */
    public ResponseBodyVO checkVersion(String appid, String clientVersion, String clientOsname, String force) {
        Map<String, Object> qryParam = Maps.newHashMap();
        qryParam.put("appid", appid);
        if ("1".equals(force)) {
            qryParam.put("force", force);
        }
        qryParam.put("orders", " rec_time desc");
        List<Map<String, Object>> list = this.query(tableName, qryParam);

        // update:升级类型：0 不升级 1：wgt升级 2：整包升级
        Map<String, Object> result = Maps.newHashMap();
        result.put("update", "0");
        if (!CollectionUtils.isEmpty(list)) {
            boolean isApk = false;// 是否是apk
            // type更新类型：1:wgt升级,2:apk升级
            // 先检查是否有apk版本升级
            for (int i = 0; i < list.size(); i++) {
                String osname = StrUtil.getMapValue(list.get(i), "osname");
                if ("2".equals(list.get(i).get("type")) && (osname.equals("all") || osname.equals(clientOsname))) {
                    // 需要版本大于客户端版本升级
                    try {
                        if (compareVersion(clientVersion, StrUtil.getMapValue(list.get(i), "version")) < 0) {
                            result.put("version", list.get(i).get("version"));
                            result.put("update", list.get(i).get("type"));
                            result.put("force", list.get(i).get("force"));
                            result.put("url", list.get(i).get("url"));
                            result.put("note", list.get(i).get("remark"));// 更新提示
                            isApk = true;
                        }
                    } catch (Exception e) {
                        System.err.println("版本参数错误：" + e.getMessage());
                    }
                    // 只比对最新的一个即可
                    break;
                }
            }
            if (!isApk) {// 没有apk版本升级
                for (int i = 0; i < list.size(); i++) {
                    String osname = StrUtil.getMapValue(list.get(i), "osname");
                    if (osname.equals("all") || osname.equals(clientOsname)) {
                        // 需要版本大于客户端版本升级
                        try {
                            if (compareVersion(clientVersion, StrUtil.getMapValue(list.get(i), "version")) < 0) {
                                result.put("version", list.get(i).get("version"));
                                result.put("update", list.get(i).get("type"));
                                result.put("force", list.get(i).get("force"));
                                result.put("url", list.get(i).get("url"));
                                result.put("note", list.get(i).get("remark"));// 更新提示
                            }
                        } catch (Exception e) {
                            System.err.println("版本参数错误：" + e.getMessage());
                        }
                        // 只比对最新的一个即可
                        break;
                    }
                }
            }
        }
        return ServerRspUtil.success(result);
    }

    /**
     * *比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * 
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }

        if (StringUtils.equals(version1, version2)) {// 相等，直接返回0
            return 0;
        }
        String[] versionArray1 = version1.split("\\.");// 注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);// 取最小长度值
        int diff = 0;
        while (idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0// 先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {// 再比较字符
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(compareVersion("1.0.2", "1.0.2.101"));
    }

    /**
     * app 注册
     * 
     * @param param
     * @return
     */
    public ResponseBodyVO regApp(Map<String, String> param) {
        param.put("user_name", SecurityUtils.getUsername());
        appMapper.disableAppInfo(param);
        appMapper.saveAppInfo(param);
        return ServerRspUtil.success("注册成功");
    }

}
