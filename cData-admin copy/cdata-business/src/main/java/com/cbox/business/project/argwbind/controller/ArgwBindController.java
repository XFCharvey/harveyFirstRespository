package com.cbox.business.project.argwbind.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;

import com.cbox.business.project.argwbind.bean.ArgwBindVO;

import com.cbox.business.project.argwbind.service.ArgwBindService;


/**
 * @ClassName: ArgwBindController
 * @Function: 阿融官微绑定
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project/argwbind")
public class ArgwBindController extends BaseController {

	@Autowired
	private ArgwBindService argwBindService;

	
	/** listArgwBind : 获取阿融官微绑定列表数据 **/
	@RequestMapping(value = "listArgwBind", method = RequestMethod.POST)
	public TableDataInfo listArgwBind(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= argwBindService.listArgwBind(param);

		return getDataTable(list);
	}

	/** getArgwBind : 获取指定id的阿融官微绑定数据 **/
	@RequestMapping(value = "getArgwBind", method = RequestMethod.POST)
	public ResponseBodyVO getArgwBind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return argwBindService.getArgwBind(param);
	}

	/** addArgwBind : 新增阿融官微绑定 **/
	@RequestMapping(value = "addArgwBind", method = RequestMethod.POST)
	public ResponseBodyVO addArgwBind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id,type,file_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return argwBindService.addArgwBind(param);
	}
	
	/** addArgwBindBatch : 批量阿融官微绑定 **/
	@RequestMapping(value = "addArgwBindBatch", method = RequestMethod.POST)
	public ResponseBodyVO addArgwBindBatch(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("project_id,type,file_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return argwBindService.addArgwBindBatch(param);
	}
	
	/** updateArgwBind : 修改阿融官微绑定 **/
	@RequestMapping(value = "updateArgwBind", method = RequestMethod.POST)
	public ResponseBodyVO updateArgwBind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return argwBindService.updateArgwBind(param);
	}

	/** delArgwBind : 删除阿融官微绑定 **/
	@RequestMapping(value = "delArgwBind", method = RequestMethod.POST)
	public ResponseBodyVO delArgwBind(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return argwBindService.delArgwBind(param);
	}


}
