package com.cbox.business.info.infocarousel.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: InfoCarouselMapper
 * @Function: 资讯轮播图
 * 
 * @author cbox
 * @version 1.0
 */
@Mapper
public interface InfoCarouselMapper {

	/** listInfoCarousel : 获取资讯轮播图列表数据 **/
	public List<Map<String, Object>> listInfoCarousel(Map<String, Object> param);

	/** getInfoCarousel : 获取指定id的资讯轮播图数据 **/
	public Map<String, Object> getInfoCarousel(Map<String, Object> param);


}
