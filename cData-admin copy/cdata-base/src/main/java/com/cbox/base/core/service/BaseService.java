package com.cbox.base.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.mapper.BaseCRUDMapper;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;

@Service
public class BaseService {

    @Autowired
    private BaseCRUDMapper baseCRUDMapper;

    /** 添加用户信息到参数中 */
    public void appendUserInfo(Map<String, Object> params) {
        params.put("curr_user_id", SecurityUtils.getUsername()); // 当前用户id
        params.put("up_dept_auth", SecurityUtils.getUpDeptAuth()); // 当前用户向上的部门权限，多个逗号分隔
    }

    /**
     * 保存，自动插入rec_系列的字段
     */
    public int save(String tableName, Map<String, Object> params) {

        if (StrUtil.isNull(params.get("rec_person"))) {
            params.put("rec_person", SecurityUtils.getUsername());
        }
        if (StrUtil.isNull(params.get("rec_updateperson"))) {
            params.put("rec_updateperson", SecurityUtils.getUsername());
        }

        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return baseCRUDMapper.save(tableMap);
    }

    /**
     * 保存，不插入rec_系列字段
     */
    public int saveNoRec(String tableName, Map<String, Object> params) {

        return saveNoRec(tableName, params, false);
    }

    /**
     * saveNoRec: 保存不插入rec_系列字段，根据bHasRecId判断是否返回自增的rec_id。
     *
     * @date: 2021年6月29日 上午9:49:42 
     * @author qiuzq 
     * @param tableName
     * @param params
     * @param bHasRecId true-返回自增rec_id，false-只保存插入
     * @return
     */
    public int saveNoRec(String tableName, Map<String, Object> params, boolean bHasRecId) {

        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);

        if (bHasRecId) {
            return baseCRUDMapper.saveNoRecHasRecId(tableMap);
        } else {
            return baseCRUDMapper.saveNoRec(tableMap);
        }
    }

    /**
     * saveNoRec: 保存不插入rec_系列字段，根据bHasRecId判断是否返回自增的rec_id。
     *
     * @date: 2021年7月14日 晚上19:40:34 
     * @author huangfx 
     * @param tableName
     * @param params
     * @return 会返回新增的dept_id
     */
    public int saveDept(String tableName, Map<String, Object> params) {

        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return baseCRUDMapper.saveDeptHasDeptId(tableMap);
    }

    public int saveUser(String tableName, Map<String, Object> params) {

        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return baseCRUDMapper.saveUserHasUserId(tableMap);
    }

    /**
     * 更新带条件
     */
    public int updateNoRec(Map<String, Object> conditions, String tableName, Map<String, Object> params) {

        Map<String, Object> tableMap = this.updateBase(tableName, params);

        tableMap.put("conditions", conditions);
        return baseCRUDMapper.updateConditionsNoRec(tableMap);
    }

    public int update(Map<String, Object> conditions, String tableName, Map<String, Object> params) {

        if (StrUtil.isNull(params.get("rec_updateperson"))) {
            params.put("rec_updateperson", SecurityUtils.getUsername());
        }

        Map<String, Object> tableMap = this.updateBase(tableName, params);

        tableMap.put("conditions", conditions);
        return baseCRUDMapper.updateConditions(tableMap);
    }

    /**
     * 更新按主键
     */
    public int update(String rec_id, String tableName, Map<String, Object> params) {

        if (StringUtils.isBlank(rec_id)) {
            return -1;
        }

        if (StrUtil.isNull(params.get("rec_updateperson"))) {
            params.put("rec_updateperson", SecurityUtils.getUsername());
        }

        Map<String, Object> tableMap = this.updateBase(tableName, params);
        tableMap.put("rec_id", rec_id);
        return baseCRUDMapper.update(tableMap);

    }

    /**
     * 更新自增。params：key-column,value 自增的数量（数字）
     */
    public int updateIncrement(Map<String, Object> conditions, String tableName, Map<String, Object> params) {

        Map<String, Object> tableMap = this.updateBase(tableName, params);
        tableMap.put("conditions", conditions);
        return baseCRUDMapper.updateIncrement(tableMap);
    }

    /**
     * 更新自减。params：key-column,value 自减的数量（数字）
     */
    public int updateDecrement(Map<String, Object> conditions, String tableName, Map<String, Object> params) {

        Map<String, Object> tableMap = this.updateBase(tableName, params);
        tableMap.put("conditions", conditions);
        return baseCRUDMapper.updateDecrement(tableMap);
    }

    private Map<String, Object> updateBase(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);

        return tableMap;
    }

    /**
     * 删除
     * 
     * @param userName
     */
    public int delete(String tableName, Map<String, Object> params) {

        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);

        tableMap.put("rec_updateperson", SecurityUtils.getUsername());
        return baseCRUDMapper.delete(tableMap);
    }

    /**
     * 物理删除
     * 
     * @param userName
     */
    public int deleteEmpty(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return baseCRUDMapper.deleteEmpty(tableMap);
    }

    /**
     * 查询列表
     */
    public List<Map<String, Object>> query(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = this.queryParam(tableName, params);
        return baseCRUDMapper.query(tableMap);
    }

    public List<Map<String, Object>> queryNoRec(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = this.queryParam(tableName, params);
        tableMap.put("rec", "1");
        return baseCRUDMapper.query(tableMap);
    }

    private Map<String, Object> queryParam(String tableName, Map<String, Object> params) {
        String orders = "";
        if (null != params.get("orders") && StringUtils.isNotBlank(params.get("orders").toString())) {
            orders = params.get("orders").toString();
            params.remove("orders");
        }
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        tableMap.put("orders", orders);
        return tableMap;
    }

    /**
     * 模糊查询列表
     */
    public List<Map<String, Object>> queryLike(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return baseCRUDMapper.queryLike(tableMap);
    }

    /**
     * 查询详情
     */
    public Map<String, Object> queryOne(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = this.queryOneParam(tableName, params);
        return baseCRUDMapper.queryOne(tableMap);
    }

    public Map<String, Object> queryOneNoRec(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = this.queryOneParam(tableName, params);
        tableMap.put("rec", "1");
        return baseCRUDMapper.queryOne(tableMap);
    }

    private Map<String, Object> queryOneParam(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return tableMap;
    }

    /**
     * 查询指定字段列的单条数据 精准
     * 
     * @param tableName 表名
     * @param params
     * @param columns 查询字段列集合
     * @return
     */
    public Map<String, Object> queryOne(String tableName, Map<String, Object> params, List<String> columns) {
        Map<String, Object> tableMap = this.queryOneParam(tableName, params, columns);
        return baseCRUDMapper.queryOne(tableMap);
    }

    public Map<String, Object> queryOneNoRec(String tableName, Map<String, Object> params, List<String> columns) {
        Map<String, Object> tableMap = this.queryOneParam(tableName, params, columns);
        tableMap.put("rec", "1");
        return baseCRUDMapper.queryOne(tableMap);
    }

    private Map<String, Object> queryOneParam(String tableName, Map<String, Object> params, List<String> columns) {
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        tableMap.put("columns", columns);
        return tableMap;
    }

    /**
     * count:查询总记录数，支持=参数和like参数 params
     *
     * @author cbox
     * @param tableName
     * @param params 默认是=参数；如果里面含有likeParams，则提取出来作为like参数
     * @return
     */
    public int count(String tableName, Map<String, Object> params) {
        Map<String, Object> tableMap = new HashMap<String, Object>();

        // 含有like的参数
        Object likeObject = params.get("likeParams");
        if (null != likeObject && likeObject instanceof Map) {
            tableMap.put("likeParams", params.get("likeParams"));
        } else {
            tableMap.put("likeParams", new HashMap());
        }
        params.remove("likeParams");

        tableMap.put("tableName", tableName);
        tableMap.put("params", params);
        return baseCRUDMapper.count(tableMap);
    }

}
