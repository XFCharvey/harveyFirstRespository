package com.cbox.business.rangemanage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RangeManageMapper
 * @Function: 片区管理
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper

public interface RangeManageMapper {

	/** listRangeManage : 获取片区管理列表数据 **/
	public List<Map<String, Object>> listRangeManage(Map<String, Object> param);

	/** getRangeManage : 获取指定id的片区管理数据 **/
	public Map<String, Object> getRangeManage(Map<String, Object> param);


}
