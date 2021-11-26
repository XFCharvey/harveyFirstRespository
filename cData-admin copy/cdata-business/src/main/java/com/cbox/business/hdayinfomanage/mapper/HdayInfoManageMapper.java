package com.cbox.business.hdayinfomanage.mapper;
 
import java.util.List;
import java.util.Map;
 
import org.apache.ibatis.annotations.Mapper;
 
/**
 * @ClassName: HdayInfoManageMapper
 * @Function: 节日信息管理
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface HdayInfoManageMapper {
 
    /** listHdayInfoManage : 获取节日信息管理列表数据 **/
    public List<Map<String, Object>> listHdayInfoManage(Map<String, Object> param);
 
    /** getHdayInfoManage : 获取指定id的节日信息管理数据 **/
    public Map<String, Object> getHdayInfoManage(Map<String, Object> param);
 
 
}