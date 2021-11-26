package com.cbox.reward.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.reward.service.RewardService;

/**
 * 中间短信
 *
 */
@RestController
@RequestMapping("/lottery")
public class RewardCashController extends BaseController {
    
    @Autowired
    RewardService rewardService;

    /**
     * 发送奖品对接短信
     */
    @PostMapping("sendSms")
    public ResponseBodyVO sendSms(@RequestBody Map<String, Object> param) throws ClientException {
        int result = rewardService.sendSms();
        return ServerRspUtil.success(result);
    }

}