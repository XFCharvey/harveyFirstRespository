package com.cbox.common.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.common.service.QrCodeLoginService;

/**
 * 二维码登录验证
 */
@RestController
@RequestMapping("")
public class QrCodeLoginController {

    @Autowired
    private QrCodeLoginService service;

    /**
     * login：生成二维码 （无需登录）
     * 
     * @param loginBody
     * @return
     */
    @PostMapping("/login/qrCode")
    public AjaxResult loginQrCode(@RequestBody Map<String, Object> param) {
        return service.genQrCode("0");
    }

    /**
     * qrCode：生成二维码 （需登录）
     * 
     * @param loginBody
     * @return
     */
    @PostMapping("/qrCode")
    public AjaxResult qrCode(@RequestBody Map<String, Object> param) {
        return service.genQrCode("1");
    }

    /**
     * loginCheckQrCode:轮训 unid是否已登录 （不需要登录）
     * 
     * @param param
     * @return
     */
    @PostMapping("/login/checkQrCode")
    public ResponseBodyVO loginCheckQrCode(@RequestBody Map<String, String> param) {
        // 验证码信息
        String uuid = param.get("uuid");
        if (StringUtils.isEmpty(uuid)) {
            return ServerRspUtil.error("缺少请求参数");
        }
        return service.checkQrCode(uuid);
    }

    /**
     * checkQrCode:轮训 unid是否已登录 （需要登录后）
     * 
     * @param param
     * @return
     */
    @PostMapping("/checkQrCode")
    public ResponseBodyVO checkQrCode(@RequestBody Map<String, String> param) {
        // 验证码信息
        String uuid = param.get("uuid");
        if (StringUtils.isEmpty(uuid)) {
            return ServerRspUtil.error("缺少请求参数");
        }
        return service.checkQrCode(uuid);
    }

    /**
     * submitScanCode: 扫码二维码结果提交
     * 
     * @param param
     * @return
     */
    @PostMapping("/submitScanCode")
    public ResponseBodyVO submitScanCode(@RequestBody Map<String, String> param) {
        String uuid = param.get("uuid");// 扫码的二维码
        String result = param.get("result");// 扫码结果
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(result)) {
            return ServerRspUtil.error("缺少请求参数");
        }
        return service.submitQrCode(uuid, result);
    }

}
