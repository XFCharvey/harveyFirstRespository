package com.cbox.business.info.infocontent.controller;

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
import com.cbox.business.info.infocontent.service.InfoContentService;


/**
 * @ClassName: InfoContentController
 * @Function: 资讯信息
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/info/infocontent")
public class InfoContentController extends BaseController {

	@Autowired
	private InfoContentService infoContentService;

	
	/** listInfoContent : 获取资讯信息列表数据 **/
	@RequestMapping(value = "listInfoContent", method = RequestMethod.POST)
	public TableDataInfo listInfoContent(@RequestBody Map<String, Object> param) {

		startPage();
        List<Map<String, Object>> list = infoContentService.listInfoContent(param, true);

		return getDataTable(list);
	}

    /** listInfoContentByPerson : 获取当前登录用户发布的资讯信息列表数据 **/
    @RequestMapping(value = "listInfoContentByPerson", method = RequestMethod.POST)
    public TableDataInfo listInfoContentByPerson(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = infoContentService.listInfoContentByPerson(param);

        return getDataTable(list);
    }

    /** listInfoContent : 获取资讯信息列表数据 **/
    @RequestMapping(value = "listInfoContentApp", method = RequestMethod.POST)
    public TableDataInfo listInfoContentApp(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = infoContentService.listInfoContent(param, false);

        return getDataTable(list);
    }

	/** getInfoContent : 获取指定id的资讯信息数据 **/
	@RequestMapping(value = "getInfoContent", method = RequestMethod.POST)
	public ResponseBodyVO getInfoContent(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoContentService.getInfoContent(param);
	}

	/** addInfoContent : 新增资讯信息 **/
	@RequestMapping(value = "addInfoContent", method = RequestMethod.POST)
	public ResponseBodyVO addInfoContent(@RequestBody Map<String, Object> param) {

		// 校验必填参数
        String checkResult = this.validInput("info_menuid,info_title", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoContentService.addInfoContent(param);
	}

	/** updateInfoContent : 修改资讯信息 **/
	@RequestMapping(value = "updateInfoContent", method = RequestMethod.POST)
	public ResponseBodyVO updateInfoContent(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoContentService.updateInfoContent(param);
	}

    /** updateInfoContent : 修改资讯信息 **/
    @RequestMapping(value = "updateInfoContentStatus", method = RequestMethod.POST)
    public ResponseBodyVO updateInfoContentStatus(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id,info_status", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return infoContentService.updateInfoContentStatus(param);
    }

	/** delInfoContent : 删除资讯信息 **/
	@RequestMapping(value = "delInfoContent", method = RequestMethod.POST)
	public ResponseBodyVO delInfoContent(@RequestBody Map<String, Object> param) {

		// 校验必填参数
		String checkResult = this.validInput("rec_id", param);
		if (!VALID_SUCC.equals(checkResult)) {
			return ServerRspUtil.error(checkResult);
		}

		return infoContentService.delInfoContent(param);
	}
	
    /** 发现-推荐列表 **/
    @RequestMapping(value = "listInfoRecommend", method = RequestMethod.POST)
    public TableDataInfo listInfoRecommend(@RequestBody Map<String, Object> param) {
        startPage();
        List<Map<String, Object>> list = infoContentService.listInfoRecommend(param);
        return getDataTable(list);
    }

    /** 压缩资讯图片，这个接口用作单独调用手工压缩修改历史数据**/
    @RequestMapping(value = "compressTitleImg", method = RequestMethod.POST)
    public ResponseBodyVO compressTitleImg(@RequestBody Map<String, Object> param) {
        return infoContentService.compressTitleImg(param);
    }


}
