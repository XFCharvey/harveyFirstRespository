package com.cbox.business.info.infocarousel.controller;

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
import com.cbox.business.info.infocarousel.service.InfoCarouselService;


/**
 * @ClassName: InfoCarouselController
 * @Function: 资讯轮播图
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/info/infocarousel")
public class InfoCarouselController extends BaseController {

	@Autowired
	private InfoCarouselService infoCarouselService;

	
	/** listInfoCarousel : 获取资讯轮播图列表数据 **/
	@RequestMapping(value = "listInfoCarousel", method = RequestMethod.POST)
	public TableDataInfo listInfoCarousel(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= infoCarouselService.listInfoCarousel(param);

		return getDataTable(list);
	}

	/** getInfoCarousel : 获取指定id的资讯轮播图数据 **/
	@RequestMapping(value = "getInfoCarousel", method = RequestMethod.POST)
	public ResponseBodyVO getInfoCarousel(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoCarouselService.getInfoCarousel(param);
	}

	/** addInfoCarousel : 新增资讯轮播图 **/
	@RequestMapping(value = "addInfoCarousel", method = RequestMethod.POST)
	public ResponseBodyVO addInfoCarousel(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("carousel_img,carousel_level", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoCarouselService.addInfoCarousel(param);
	}

	/** updateInfoCarousel : 修改资讯轮播图 **/
	@RequestMapping(value = "updateInfoCarousel", method = RequestMethod.POST)
	public ResponseBodyVO updateInfoCarousel(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoCarouselService.updateInfoCarousel(param);
	}

	/** delInfoCarousel : 删除资讯轮播图 **/
	@RequestMapping(value = "delInfoCarousel", method = RequestMethod.POST)
	public ResponseBodyVO delInfoCarousel(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoCarouselService.delInfoCarousel(param);
	}


}
