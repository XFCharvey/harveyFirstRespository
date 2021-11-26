package com.cbox.business.rangemanage.controller;

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


import com.cbox.business.rangemanage.service.RangeManageService;


/**
 * @ClassName: RangeManageController
 * @Function: 片区管理
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/rangemanage")
public class RangeManageController extends BaseController {

	@Autowired
	private RangeManageService rangeManageService;

	
	/** listRangeManage : 获取片区管理列表数据 **/
	@RequestMapping(value = "listRangeManage", method = RequestMethod.POST)
	public TableDataInfo listRangeManage(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= rangeManageService.listRangeManage(param);

		return getDataTable(list);
	}

	/** getRangeManage : 获取指定id的片区管理数据 **/
	@RequestMapping(value = "getRangeManage", method = RequestMethod.POST)
	public ResponseBodyVO getRangeManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return rangeManageService.getRangeManage(param);
	}

	/** addRangeManage : 新增片区管理 **/
	@RequestMapping(value = "addRangeManage", method = RequestMethod.POST)
	public ResponseBodyVO addRangeManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("p_username,position_value,position_label,position_desc", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return rangeManageService.addRangeManage(param);
	}

	/** updateRangeManage : 修改片区管理 **/
	@RequestMapping(value = "updateRangeManage", method = RequestMethod.POST)
	public ResponseBodyVO updateRangeManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return rangeManageService.updateRangeManage(param);
	}

	/** delRangeManage : 删除片区管理 **/
	@RequestMapping(value = "delRangeManage", method = RequestMethod.POST)
	public ResponseBodyVO delRangeManage(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return rangeManageService.delRangeManage(param);
	}


}
