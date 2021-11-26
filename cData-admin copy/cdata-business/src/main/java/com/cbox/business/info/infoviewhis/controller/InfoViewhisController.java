package com.cbox.business.info.infoviewhis.controller;

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
import com.cbox.business.info.infoviewhis.service.InfoViewhisService;


/**
 * @ClassName: InfoViewhisController
 * @Function: 浏览历史
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/info/infoviewhis")
public class InfoViewhisController extends BaseController {

	@Autowired
	private InfoViewhisService infoViewhisService;

	
	/** listInfoViewhis : 获取浏览历史列表数据 **/
	@RequestMapping(value = "listInfoViewhis", method = RequestMethod.POST)
	public TableDataInfo listInfoViewhis(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= infoViewhisService.listInfoViewhis(param);

		return getDataTable(list);
	}

	/** getInfoViewhis : 获取指定id的浏览历史数据 **/
	@RequestMapping(value = "getInfoViewhis", method = RequestMethod.POST)
	public ResponseBodyVO getInfoViewhis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoViewhisService.getInfoViewhis(param);
	}

	/** addInfoViewhis : 新增浏览历史 **/
	@RequestMapping(value = "addInfoViewhis", method = RequestMethod.POST)
	public ResponseBodyVO addInfoViewhis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("info_id,view_person,view_time", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoViewhisService.addInfoViewhis(param);
	}

	/** delInfoViewhis : 删除浏览历史 **/
	@RequestMapping(value = "delInfoViewhis", method = RequestMethod.POST)
	public ResponseBodyVO delInfoViewhis(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoViewhisService.delInfoViewhis(param);
	}

    /** delInfoViewhis : 清空该用户浏览历史 **/
    @RequestMapping(value = "delallInfoViewhis", method = RequestMethod.POST)
    public ResponseBodyVO delallInfoViewhis(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("view_person", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return infoViewhisService.delallInfoViewhis(param);
    }


}
