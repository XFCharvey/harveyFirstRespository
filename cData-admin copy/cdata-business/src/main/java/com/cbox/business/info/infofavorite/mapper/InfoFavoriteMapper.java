package com.cbox.business.info.infofavorite.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: InfoFavoriteMapper
 * @Function: 资讯收藏
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface InfoFavoriteMapper {

	/** listInfoFavorite : 获取资讯收藏列表数据 **/
	public List<Map<String, Object>> listInfoFavorite(Map<String, Object> param);

	/** getInfoFavorite : 获取指定id的资讯收藏数据 **/
	public Map<String, Object> getInfoFavorite(Map<String, Object> param);


}
