package com.cbox.business.info.infocarousel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.business.info.infocarousel.mapper.InfoCarouselMapper;


/**
 * @ClassName: InfoCarouselService
 * @Function: 资讯轮播图
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class InfoCarouselService extends BaseService {

    @Autowired
    private InfoCarouselMapper infoCarouselMapper;
    
	/** listInfoCarousel : 获取资讯轮播图列表数据 **/
	public List<Map<String, Object>> listInfoCarousel(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = infoCarouselMapper.listInfoCarousel(param);

		return list;
	}

	/** getInfoCarousel : 获取指定id的资讯轮播图数据 **/
	public ResponseBodyVO getInfoCarousel(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = infoCarouselMapper.getInfoCarousel(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addInfoCarousel : 新增资讯轮播图 **/
	public ResponseBodyVO addInfoCarousel(Map<String, Object> param) {

		// Table:d_carousel
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("carousel_img", param.get("carousel_img"));
		mapParam.put("carousel_title", param.get("carousel_title"));
		mapParam.put("info_menu_id", param.get("info_menu_id"));
        mapParam.put("relation_id", param.get("relation_id"));
        mapParam.put("carousel_type", param.get("carousel_type"));
		mapParam.put("carousel_level", param.get("carousel_level"));
		mapParam.put("carousel_status", param.get("carousel_status"));
		int count = this.save( "d_carousel", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增资讯轮播图失败");
	}

	/** updateInfoCarousel : 修改资讯轮播图 **/
	public ResponseBodyVO updateInfoCarousel(Map<String, Object> param) {

		// Table:d_carousel
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("carousel_img", param.get("carousel_img"));
		mapParam.put("carousel_title", param.get("carousel_title"));
		mapParam.put("info_menu_id", param.get("info_menu_id"));
        mapParam.put("relation_id", param.get("relation_id"));
        mapParam.put("carousel_type", param.get("carousel_type"));
		mapParam.put("carousel_level", param.get("carousel_level"));
		mapParam.put("carousel_status", param.get("carousel_status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_carousel", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改资讯轮播图失败"); 
	}

	/** delInfoCarousel : 删除资讯轮播图 **/
	public ResponseBodyVO delInfoCarousel(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_carousel
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_carousel",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除资讯轮播图失败");
	}


}
