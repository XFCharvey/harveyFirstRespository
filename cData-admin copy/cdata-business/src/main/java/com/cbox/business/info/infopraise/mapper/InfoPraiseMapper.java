package com.cbox.business.info.infopraise.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: InfoPraiseMapper
 * @Function: 资讯点赞
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface InfoPraiseMapper {

	/** listInfoPraise : 获取资讯点赞列表数据 **/
	public List<Map<String, Object>> listInfoPraise(Map<String, Object> param);

	/** getInfoPraise : 获取指定id的资讯点赞数据 **/
	public Map<String, Object> getInfoPraise(Map<String, Object> param);

    public Map<String, Object> getUserInfoPraiseTotal(Map<String, Object> mapInfoPraiseCondition);

}
