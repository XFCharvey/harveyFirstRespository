package com.cbox.business.info.infopraise.controller;

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
import com.cbox.business.info.infopraise.service.InfoPraiseService;

/**
 * @ClassName: InfoPraiseController
 * @Function: 资讯点赞
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/info/infopraise")
public class InfoPraiseController extends BaseController {

	@Autowired
	private InfoPraiseService infoPraiseService;

	/** listInfoPraise : 获取资讯点赞列表数据 **/
	@RequestMapping(value = "listInfoPraise", method = RequestMethod.POST)
	public TableDataInfo listInfoPraise(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list = infoPraiseService.listInfoPraise(param);

		return getDataTable(list);
	}

	/** getInfoPraise : 获取指定id的资讯点赞数据 **/
	@RequestMapping(value = "getInfoPraise", method = RequestMethod.POST)
	public ResponseBodyVO getInfoPraise(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoPraiseService.getInfoPraise(param);
	}

	/** addInfoPraise : 新增资讯点赞 **/
	@RequestMapping(value = "addInfoPraise", method = RequestMethod.POST)
	public ResponseBodyVO addInfoPraise(@RequestBody Map<String, Object> param) {

		return infoPraiseService.addInfoPraise(param);
	}

	/** delInfoPraise : 删除资讯点赞 **/
	@RequestMapping(value = "delInfoPraise", method = RequestMethod.POST)
	public ResponseBodyVO delInfoPraise(@RequestBody Map<String, Object> param) {

		return infoPraiseService.delInfoPraise(param);
	}

}
