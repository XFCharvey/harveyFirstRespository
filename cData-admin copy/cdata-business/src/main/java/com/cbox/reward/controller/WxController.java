package com.cbox.reward.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@RestController
@RequestMapping("/wechat")
public class WxController extends BaseController {

    @Autowired
    private WxMpService wxMpService;

    /**
     * * 获取Jsapi signature
     * 
     * @throws WxErrorException
     */
    @PostMapping("signature")
    public ResponseBodyVO signature(@RequestBody Map<String, String> param) throws WxErrorException {
        String url = param.get("url");
        System.out.println("url:" + url);
        System.out.println("getAccessToken:" + wxMpService.getAccessToken());
        System.out.println("getJsapiTicket:" + wxMpService.getJsapiTicket());
        WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);
        System.out.println(JSON.toJSON(wxJsapiSignature));
        return ServerRspUtil.success(wxJsapiSignature);
    }

}