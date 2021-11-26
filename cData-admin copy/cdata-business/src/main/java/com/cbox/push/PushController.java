package com.cbox.push;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;

@RestController
@RequestMapping("/push")
public class PushController extends BaseController {

    @Autowired
    PushUtil pushUtil;

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public ResponseBodyVO send(@RequestBody Map<String, Object> param) {
        System.out.println(param);
        //
        String clientId = "c649fe60cd456789784d4c70e52446dd";
        JSONObject pay = new JSONObject();
        pay.put("type", "1");
        pay.put("value", "888");
        pushUtil.send("Android", "测试ti888", "消息内容测试888", clientId, pay);
        return ServerRspUtil.success(param);
    }
}