package com.cbox.business.info.infofavorite.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.info.infofavorite.service.InfoFavoriteService;


/**
 * @ClassName: InfoFavoriteController
 * @Function: 资讯收藏
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/info/infofavorite")
public class InfoFavoriteController extends BaseController {

	@Autowired
	private InfoFavoriteService infoFavoriteService;

	
	/** listInfoFavorite : 获取资讯收藏列表数据 **/
	@RequestMapping(value = "listInfoFavorite", method = RequestMethod.POST)
	public TableDataInfo listInfoFavorite(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= infoFavoriteService.listInfoFavorite(param);

		return getDataTable(list);
	}

	/** getInfoFavorite : 获取指定id的资讯收藏数据 **/
	@RequestMapping(value = "getInfoFavorite", method = RequestMethod.POST)
	public ResponseBodyVO getInfoFavorite(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoFavoriteService.getInfoFavorite(param);
	}

	/** addInfoFavorite : 新增资讯收藏 **/
	@RequestMapping(value = "addInfoFavorite", method = RequestMethod.POST)
	public ResponseBodyVO addInfoFavorite(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("info_id,f_person,f_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoFavoriteService.addInfoFavorite(param);
	}


	/** delInfoFavorite : 删除资讯收藏 **/
	@RequestMapping(value = "delInfoFavorite", method = RequestMethod.POST)
	public ResponseBodyVO delInfoFavorite(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("info_id,f_person", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoFavoriteService.delInfoFavorite(param);
	}


}
