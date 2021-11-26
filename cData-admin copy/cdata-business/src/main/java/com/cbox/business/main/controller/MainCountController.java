package com.cbox.business.main.controller;

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
import com.cbox.business.main.service.MainCountService;

/**
 * @ClassName: MainCountController
 * @Function: 相关统计
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/main/maincount")
public class MainCountController extends BaseController {

	@Autowired
	private MainCountService mainCountService;

	/** getmaincount : 获取app首页客户总数，阿隆、官微专注总数，风险预警及新增总数，阶段调研总数 **/
	@RequestMapping(value = "getmaincount", method = RequestMethod.POST)
	public ResponseBodyVO getmaincount(@RequestBody Map<String, Object> param) {

		return mainCountService.getmaincount(param);
	}

}