package com.cbox.framework.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.core.domain.entity.SysDept;
import com.cbox.base.core.domain.entity.SysUser;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;

/**
 * @ClassName: AuthLevelService 
 * @Function: 层级权限  
 * 
 * @author qiu 
 * @date 2021年4月9日 上午10:22:24 
 * @version 1.0
 */
@Service
@Transactional
public class AuthLevelService extends BaseService {

    /** 获得用户的分级权限，返回以逗号分隔的字符串 */
    public String getUserLevelAuth(SysUser user, boolean isSelf) {

        // 得到当前用户
        String userId = user.getUserName();

        Map<String, String> mapLevels = new HashMap<String, String>();
        List<String> listLevels = new ArrayList<String>();

        // 获取分配给当前用户的分级权限
        Map<String, Object> mapQueryParam0 = new HashMap<String, Object>();
        mapQueryParam0.put("user_id", user.getUserId());
        List<Map<String, Object>> listLevel = this.queryNoRec("sys_user_post", mapQueryParam0);
        if (listLevel != null) {
            for (Map<String, Object> map : listLevel) {
                listLevels.add(StrUtil.getMapValue(map, "post_id"));
            }
        }

        // 获取附加分配给当前用户的分级权限
        if (!isSelf) {
            Map<String, Object> mapQueryParam1 = new HashMap<String, Object>();
            mapQueryParam1.put("user_id", userId);
            List<Map<String, Object>> listLevelAdd = this.queryNoRec("sys_post_auth", mapQueryParam1);
            if (listLevelAdd != null) {
                for (Map<String, Object> map : listLevelAdd) {
                    listLevels.add(StrUtil.getMapValue(map, "post_id"));
                }
            }
        }


        // 获取分级权限的所有子节点
        Map<String, Object> mapQueryParam2 = new HashMap<String, Object>();
        mapQueryParam2.put("status", "0");
        List<Map<String, Object>> listPost = this.queryNoRec("sys_post", mapQueryParam2);

        Map<String, String> mapPosts = new HashMap<String, String>();
        for (Map<String, Object> map : listPost) {
            mapPosts.put(StrUtil.getMapValue(map, "post_id"), StrUtil.getMapValue(map, "ancestors"));
        }

        // 得到当前用户的实际分级权限范围
        for (int i = 0; i < listLevels.size(); i++) {
            String levelId = listLevels.get(i);
            mapLevels.put(levelId, "");

            String ancestors = mapPosts.get(levelId) + "," + levelId;
            if (StrUtil.isNotNull(ancestors)) {
                for (Map<String, Object> map : listPost) {
                    if (StrUtil.getMapValue(map, "ancestors").startsWith(ancestors)) {
                        mapLevels.put(StrUtil.getMapValue(map, "post_id"), "");
                    }
                }
            }
        }

        String levels = "";

        for (Map.Entry<String, String> entry : mapLevels.entrySet()) {
            levels += entry.getKey() + ",";
        }
        if (StrUtil.isNotNull(levels)) {
            levels = levels.substring(0, levels.length() - 1);
        }

        return levels;
    }

    /** 获得用户所属部门的向上权限（包含自身），多个逗号分隔 */
    public String getUserUpDeptAuth(SysUser user) {
        SysDept deptVO = user.getDept();
        String upDeptAuth = "" + deptVO.getDeptId();
        if (deptVO.getAncestors() != null && !"".equals(deptVO.getAncestors())) {
            upDeptAuth = deptVO.getAncestors() + "," + deptVO.getDeptId();
        }

        return upDeptAuth;
    }
    
    public void addUserLevelDeptAuth(SysUser user) {
        // 设置用户的级别信息
        // user.setLevelAuth(this.getUserLevelAuth(user, false));
        // user.setLevelSelfAuth(this.getUserLevelAuth(user, true));
        // 设置用户的部门向上和向下的权限信息
        user.setUpDeptAuth(this.getUserUpDeptAuth(user));
    }

}
