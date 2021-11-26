package com.cbox.wxlogin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.wxlogin.service.DeptSelectService;

@RestController
@RequestMapping("/wechat")
public class DeptSelectController extends BaseController {
    @Autowired
    DeptSelectService deptSelectService;

    /** listInfoMenutree : 获取资讯分类目录列表返回树形数据 **/
    @RequestMapping(value = "listDepttree", method = RequestMethod.POST)
    public ResponseBodyVO listDepttree(@RequestBody Map<String, Object> param) {

        return deptSelectService.listDepttree(param);
    }
}
