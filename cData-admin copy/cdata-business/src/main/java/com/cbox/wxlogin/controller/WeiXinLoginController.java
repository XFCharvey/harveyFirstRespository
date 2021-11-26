package com.cbox.wxlogin.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.cbox.base.constant.Constants;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.redis.RedisCache;
import com.cbox.base.utils.RandomUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.common.AliSmsConstant;
import com.cbox.common.service.AliSendSmsService;
import com.cbox.wxlogin.service.WeixinUserService;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/wechat")

public class WeiXinLoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WeixinUserService weixinUserService;
    @Autowired
    private AliSendSmsService aliSendSmsService;
    @Autowired
    private RedisCache redisCache;
    @Value(value = "${ali.sms.loginTemplate}")
    private String loginTemplate;// 登录模板

    /**
     * login: 微信登录
     * 
     */
    @PostMapping(value = "/login")
    public AjaxResult login(@RequestBody Map<String, String> param) {
        String openid = param.get("openid");
        String unionid = param.get("unionid");
        String access_token = param.get("access_token");
        String refresh_token = param.get("refresh_token");
        String expires_in = param.get("expires_in");
        String scope = param.get("scope");
        System.out.println(param);

        System.out.println("unionid=" + unionid);

        return weixinUserService.login(unionid, openid, access_token);
    }

    @PostMapping(value = "/sendsms")
    public ResponseBodyVO sendsms(@RequestBody Map<String, String> param) throws ClientException {
        String phone = param.get("phone");
        if (StringUtils.isEmpty(phone)) {
            return ServerRspUtil.error("手机号不能为空");
        }

        String code = RandomUtil.randomInt(4);// 随机数字验证码
        param.put("code", code);
        // 发送短信验证码
        Map<String, String> map = aliSendSmsService.sendSms(loginTemplate, phone, param);
        // 请求状态码。 返回OK代表请求成功。
        String retCode = map.get(AliSmsConstant.RETURN_CODE);
        if (AliSmsConstant.OK.equals(retCode)) {
            String requestId = map.get("RequestId");
            // 验证码过期 5分钟
            redisCache.setCacheObject(requestId, (phone + ":" + code), Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            return ServerRspUtil.success("success", requestId);
        } else {
            // 状态码的描述。
            String retMsg = map.get(AliSmsConstant.RETURN_MESSAGE);
            logger.error("发送短信异常！状态码：{}，描述{}", retCode, retMsg);
            return ServerRspUtil.error("发送失败:" + retMsg);
        }
    }

    /**
     * bindPhone: 绑定手机号
     * 
     * @param param
     * @return result="1" 表示执行成功， 其他失败
     */
    @PostMapping(value = "/bindPhone")
    public ResponseBodyVO bindPhone(@RequestBody Map<String, String> param) {
        String requestId = param.get("requestId");
        String phone = param.get("phone");
        String verCode = param.get("verCode");
        String wxRecId = param.get("wxRecId");
        logger.info(JSON.toJSONString(param));
        if (StringUtils.isEmpty(requestId) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(verCode) || StringUtils.isEmpty(wxRecId)) {
            return ServerRspUtil.error("请求无效!");
        }
        String value = redisCache.getCacheObject(requestId);
        if (StringUtils.isNotNull(value)) {
            String aa[] = value.split(":");
            String tempPhone = aa[0];
            String tempCode = aa[1];
            if (tempPhone.equals(phone) && tempCode.equals(verCode)) {
                redisCache.deleteObject(requestId);// 删除验证码
                Map<String, Object> data = weixinUserService.getSysUser(tempPhone);
                if (CollectionUtils.isEmpty(data)) {// 未导入 需要填写信息
                    String userId = weixinUserService.saveSysUser(wxRecId, tempPhone);
                    Map<String, String> result = Maps.newHashMap();
                    result.put("type", "1");
                    result.put("userId", userId);
                    // 跳转到填写信息页面
                    return ServerRspUtil.success(result);
                } else {// 已存在
                    String userId = StrUtil.getMapValue(data, "user_id");
                    // 0-待审核，1-审核通过，2-审核不通过 帐号状态：0-未激活，1-正常，2-待审核，3-出队，4-审核不通过
                    String status = StrUtil.getMapValue(data, "status");
                    if ("0".equals(status)) {// 导入，待激活状态
                        weixinUserService.updateUser(wxRecId, userId);// 更新用户id
                        // 返回token
                        Map<String, String> tokenMap = weixinUserService.getTokenData(userId);
                        Map<String, String> result = Maps.newHashMap();
                        result.put("type", "0");
                        result.putAll(tokenMap);
                        return ServerRspUtil.success(result);
                    } else {// 如果状态不是待激活的，不允许发送短信
                        return ServerRspUtil.error("手机号已被使用，请更换一个！");
                    }
                }
            } else {
                return ServerRspUtil.error("验证码无效!");
            }
        } else {
            return ServerRspUtil.error("验证码已过期!");
        }
    }

}