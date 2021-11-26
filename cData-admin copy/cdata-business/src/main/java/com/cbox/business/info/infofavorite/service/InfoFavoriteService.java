package com.cbox.business.info.infofavorite.service;

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
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.business.info.infofavorite.mapper.InfoFavoriteMapper;


/**
 * @ClassName: InfoFavoriteService
 * @Function: 资讯收藏
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class InfoFavoriteService extends BaseService {

    @Autowired
    private InfoFavoriteMapper infoFavoriteMapper;
    
	/** listInfoFavorite : 获取资讯收藏列表数据 **/
	public List<Map<String, Object>> listInfoFavorite(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = infoFavoriteMapper.listInfoFavorite(param);

		return list;
	}

	/** getInfoFavorite : 获取指定id的资讯收藏数据 **/
	public ResponseBodyVO getInfoFavorite(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = infoFavoriteMapper.getInfoFavorite(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addInfoFavorite : 新增资讯收藏 **/
	public ResponseBodyVO addInfoFavorite(Map<String, Object> param) {
        String userName = SecurityUtils.getUsername();
        String nowTime = DateUtils.getTime();
		// Table:d_info_favorite
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("info_id", param.get("info_id"));
        mapParam.put("f_person", userName);
        mapParam.put("f_time", nowTime);
        int count = this.saveNoRec("d_info_favorite", mapParam, false);

		return ServerRspUtil.formRspBodyVO(count, "新增资讯收藏失败");
	}



	/** delInfoFavorite : 删除资讯收藏 **/
	public ResponseBodyVO delInfoFavorite(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_info_favorite
		Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("info_id", param.get("info_id"));
        mapCondition.put("f_person", param.get("f_person"));
        int count = this.deleteEmpty("d_info_favorite", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除资讯收藏失败");
	}


}
