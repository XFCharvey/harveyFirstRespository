package com.cbox.business.info.infomenu.controller;

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
import com.cbox.business.info.infomenu.service.InfoMenuService;


/**
 * @ClassName: InfoMenuController
 * @Function: 资讯分类目录
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/info/infomenu")
public class InfoMenuController extends BaseController {

	@Autowired
	private InfoMenuService infoMenuService;

	
	/** listInfoMenu : 获取资讯分类目录列表数据 **/
	@RequestMapping(value = "listInfoMenu", method = RequestMethod.POST)
	public TableDataInfo listInfoMenu(@RequestBody Map<String, Object> param) {

		startPage();
		List<Map<String, Object>> list= infoMenuService.listInfoMenu(param);

		return getDataTable(list);
	}


    /** listInfoMenu : 获取按资讯内容更新时间排序的资讯分类目录列表数据 **/
    @RequestMapping(value = "listInfoMenuSort", method = RequestMethod.POST)
    public TableDataInfo listInfoMenuSort(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = infoMenuService.listInfoMenuSort(param);

        return getDataTable(list);
    }

    /** listInfoMenuAndSmalllevel : 获取资讯分类目录本身及下级所有目录列表数据 **/
    @RequestMapping(value = "listInfoMenuAndSmalllevel", method = RequestMethod.POST)
    public TableDataInfo listInfoMenuAndSmalllevel(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = infoMenuService.listInfoMenuAndSmalllevel(param);

        return getDataTable(list);
    }

    /** listInfoMenutree : 获取资讯分类目录列表返回树形数据 **/
    @RequestMapping(value = "listInfoMenutree", method = RequestMethod.POST)
    public ResponseBodyVO listInfoMenutree(@RequestBody Map<String, Object> param) {

        return infoMenuService.listInfoMenutree(param);
    }

	/** getInfoMenu : 获取指定id的资讯分类目录数据 **/
	@RequestMapping(value = "getInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO getInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoMenuService.getInfoMenu(param);
	}

	/** addInfoMenu : 新增资讯分类目录 **/
	@RequestMapping(value = "addInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO addInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("parent_id,ancestors,menu_name,menu_desc", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoMenuService.addInfoMenu(param);
	}

	/** updateInfoMenu : 修改资讯分类目录 **/
	@RequestMapping(value = "updateInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO updateInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoMenuService.updateInfoMenu(param);
	}

	/** delInfoMenu : 删除资讯分类目录 **/
	@RequestMapping(value = "delInfoMenu", method = RequestMethod.POST)
	public ResponseBodyVO delInfoMenu(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoMenuService.delInfoMenu(param);
	}


}
