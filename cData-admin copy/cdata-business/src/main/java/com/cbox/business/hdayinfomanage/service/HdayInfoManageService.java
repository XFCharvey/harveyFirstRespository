package com.cbox.business.hdayinfomanage.service;
 
import java.util.HashMap;
 
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import com.cbox.base.annotation.DataScope;
import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.SecurityUtils;
 
import com.cbox.business.hdayinfomanage.mapper.HdayInfoManageMapper;
 
 
/**
 * @ClassName: HdayInfoManageService
 * @Function: 节日信息管理
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class HdayInfoManageService extends BaseService {
 
    @Autowired
    private HdayInfoManageMapper hdayInfoManageMapper;
    
    /** listHdayInfoManage : 获取节日信息管理列表数据 **/
    public List<Map<String, Object>> listHdayInfoManage(Map<String, Object> param) {
        super.appendUserInfo(param);
 
        List<Map<String, Object>> list = hdayInfoManageMapper.listHdayInfoManage(param);
 
        return list;
    }
 
    /** getHdayInfoManage : 获取指定id的节日信息管理数据 **/
    public ResponseBodyVO getHdayInfoManage(Map<String, Object> param) {
        super.appendUserInfo(param);
 
        Map<String, Object> mapResult = hdayInfoManageMapper.getHdayInfoManage(param);
 
        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
    }
 
    /** addHdayInfoManage : 新增节日信息管理 **/
    public ResponseBodyVO addHdayInfoManage(Map<String, Object> param) {
 
        // Table:d_holiday_info
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("holiday_date", param.get("holiday_date"));
        mapParam.put("date_name", param.get("date_name"));
        mapParam.put("sex", param.get("sex"));
        mapParam.put("content", param.get("content"));
        int count = this.save( "d_holiday_info", mapParam);
 
        return ServerRspUtil.formRspBodyVO(count, "新增节日信息管理失败");
    }
 
    /** updateHdayInfoManage : 修改节日信息管理 **/
    public ResponseBodyVO updateHdayInfoManage(Map<String, Object> param) {
 
        // Table:d_holiday_info
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("holiday_date", param.get("holiday_date"));
        mapParam.put("date_name", param.get("date_name"));
        mapParam.put("sex", param.get("sex"));
        mapParam.put("content", param.get("content"));
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.update(mapCondition, "d_holiday_info", mapParam);
 
        return ServerRspUtil.formRspBodyVO(count, "修改节日信息管理失败"); 
    }
 
    /** delHdayInfoManage : 删除节日信息管理 **/
    public ResponseBodyVO delHdayInfoManage(Map<String, Object> param) {
 
        // TODO : 删除前的逻辑判断 
 
        // Table:d_holiday_info
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.delete("d_holiday_info",  mapCondition);
 
        return ServerRspUtil.formRspBodyVO(count, "删除节日信息管理失败");
    }
 
 
}