package com.cbox.wxlogin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.wxlogin.service.UserNoTokenService;

@RestController
@RequestMapping("/wechat")
public class UserNoTokenController extends BaseController {
    @Autowired
    UserNoTokenService userNoTokenService;

    /** updateUser : 注册时补全用户信息 **/
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public ResponseBodyVO updateUser(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("user_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return userNoTokenService.updateUser(param);
    }
}
