package com.cbox.business.check.checkinfo.controller;

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
import com.cbox.business.check.checkinfo.service.CheckInfoService;


/**
 * @ClassName: CheckInfoController
 * @Function: 审核信息表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/check/checkinfo")
public class CheckInfoController extends BaseController {

	@Autowired
	private CheckInfoService checkInfoService;

	
	/** listCheckInfo : 获取审核信息表列表数据 **/
	@RequestMapping(value = "listCheckInfo", method = RequestMethod.POST)
	public TableDataInfo listCheckInfo(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= checkInfoService.listCheckInfo(param);

		return getDataTable(list);
	}

	/** getCheckInfo : 获取指定id的审核信息表数据 **/
	@RequestMapping(value = "getCheckInfo", method = RequestMethod.POST)
	public ResponseBodyVO getCheckInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return checkInfoService.getCheckInfo(param);
	}

	/** addCheckInfo : 新增审核信息表 **/
	@RequestMapping(value = "addCheckInfo", method = RequestMethod.POST)
	public ResponseBodyVO addCheckInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("check_type,relation_id,check_title,public_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return checkInfoService.addCheckInfo(param);
	}

	/** updateCheckInfo : 修改审核信息表 **/
	@RequestMapping(value = "updateCheckInfo", method = RequestMethod.POST)
	public ResponseBodyVO updateCheckInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return checkInfoService.updateCheckInfo(param);
	}

    /** updateCheckInfo : 处理审核信息表 **/
    @RequestMapping(value = "checkStatus", method = RequestMethod.POST)
    public ResponseBodyVO CheckStatus(@RequestBody List<Map<String, Object>> param) {

        return checkInfoService.checkStatu(param);
    }

	/** delCheckInfo : 删除审核信息表 **/
	@RequestMapping(value = "delCheckInfo", method = RequestMethod.POST)
	public ResponseBodyVO delCheckInfo(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return checkInfoService.delCheckInfo(param);
	}


}
