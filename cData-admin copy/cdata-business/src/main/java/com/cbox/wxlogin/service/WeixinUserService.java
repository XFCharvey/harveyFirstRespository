package com.cbox.wxlogin.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.entity.SysUser;
import com.cbox.base.core.domain.model.LoginUser;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.CboxUtils;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ExceptionUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.base.utils.id.GlobalRecIdUtil;
import com.cbox.framework.web.service.AuthLevelService;
import com.cbox.framework.web.service.SysPermissionService;
import com.cbox.framework.web.service.TokenService;
import com.cbox.system.service.ISysUserService;
import com.cbox.wxlogin.HttpClientUtil;
import com.cbox.wxlogin.NoWeixinUserException;
import com.cbox.wxlogin.bean.WeixinUser;
import com.cbox.wxlogin.config.WeixinConfig;
import com.cbox.wxlogin.mapper.WeixinUserMapper;
import com.google.common.collect.Maps;

@Service
@Transactional
public class WeixinUserService extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String wxUserTable = "sys_user_weixin";
    private final String defauleUser = "sys";

    @Autowired
    private WeixinUserMapper weixinUserMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private WeixinConfig weixinLoginConfig;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthLevelService authLevelService;

    /**
     * findByOpenId： 通过openid获取微信用户信息
     * 
     * @param openid
     * @return
     */
    public WeixinUser findByOpenId(String openid) {
        return weixinUserMapper.findByOpenId(openid);
    }

    /**
     * findByUnionid： 通过unionid获取微信用户信息
     * 
     * @param unionid
     * @return
     */
    public WeixinUser findByUnionid(String unionid) {
        return weixinUserMapper.findByUnionid(unionid);
    }

    public WeixinUser findById(String id) {
        return weixinUserMapper.findById(id);
    }

    /**
     * login： 通过openid获取微信用户，如果不存在，则创建
     * 
     * @param unionid
     * @param openid
     * @param access_token
     * @return
     */
    public AjaxResult login(String unionid, String openid, String access_token) {
        Map<String, Object> data = Maps.newHashMap();
        String code = "";
        String errmsg = "";
        // 查询用户
        WeixinUser wxUser = this.findByOpenId(openid);
        SysUser user = null;
        String recId = "";
        if (wxUser != null && StringUtils.isNotNull(wxUser.getUser_id())) {
            recId = wxUser.getRec_id();
            // 查询用户*
            user = userService.selectUserById(Long.parseLong(wxUser.getUser_id()));
            if (user != null) {
                // 0-待审核，1-审核通过，2-审核不通过 帐号状态：0-未激活，1-正常，2-待审核，3-出队，4-审核不通过
                String status = user.getStatus();
                String userName = user.getUserName();
                String nowTime = DateUtils.getTime();
                if ("1".equals(status)) {
                    code = "200";// 直接登录
                    // 生成token
                    Map<String, String> tokenMap = this.getTokenData(user);
                    data.putAll(tokenMap);

                } else if ("2".equals(status)) {
                    code = "101";// 待审核
                    errmsg = "注册申请正在审核中，审核通过后会短信通知，请耐心等候";
                } else if ("4".equals(status)) {
                    code = "102";// 审核不通过
                    errmsg = "注册申请审核不通过！";
                } else if ("3".equals(status)) {
                    code = "103";// 审核不通过
                    errmsg = "用户已出队，不能登录！";
                } else if ("5".equals(status)) { // 5-未提交资料, 仍然需要重新绑定
                    // 需要 重新提交资料
                    code = "202";
                    errmsg = "需要补充个人信息";
                    data.put("userId", user.getUserId());
                    data.put("phone", user.getPhonenumber());
                } else {
                    code = "109";
                    errmsg = "未知错误！";
                }
            } else {
                // 需要绑定手机号
                code = "201";
                errmsg = "需要绑定手机号";
            }
        } else {
            // 需要绑定手机号
            code = "201";
            errmsg = "需要绑定手机号";

            // 获取微信用户信息
            Map<String, Object> weChatInfo = getWeiXinUserInfo(openid, access_token);
            if (weChatInfo.size() > 0) {
                recId = this.saveWeixinUser(weChatInfo);
            } else {
                String message = "获取微信个人信息失败";
                logger.error(message);
                throw new NoWeixinUserException(message);
            }
        }
        data.put("code", code);
        data.put("msg", errmsg);
        data.put("wxRecId", recId);
        AjaxResult result = AjaxResult.success(data);
        result.put("openCheck", "1");
        return result;
    }

    public Map<String, String> getTokenData(String userId) {
        SysUser user = userService.selectUserById(Long.parseLong(userId));
        return getTokenData(user);
    }

    public Map<String, String> getTokenData(SysUser user) {
        Map<String, String> data = Maps.newHashMap();
        authLevelService.addUserLevelDeptAuth(user);
        LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
        // 生成token
        String token = tokenService.createToken(loginUser);
        data.put("userId", String.valueOf(user.getUserId()));
        data.put("token", token);
        data.put("username", user.getUserName());
        data.put("nickname", user.getNickName());
        data.put("user_type", user.getUserType());
        data.put("upDeptAuth", user.getUpDeptAuth());
        return data;
    }

    /**
     * getWeiXinUserInfo 调用微信接口获取用户信息
     * 
     * @param openId
     * @param accessToken
     * @return
     */
    private Map<String, Object> getWeiXinUserInfo(String openId, String accessToken) {
        StringBuffer url = new StringBuffer(weixinLoginConfig.getUserInfoUrl());
        url.append("access_token=").append(accessToken).append("&").append("openid=").append(openId).append("&").append("lang=zh_CN");
        logger.info("WeiXinLoginController ==> getPcWeiXinUserInfo(){} url: " + url);
        String resMessageString = null;
        try {
            resMessageString = HttpClientUtil.get(url.toString());
        } catch (ClientProtocolException e) {
            logger.error(ExceptionUtil.getExceptionMessage(e));
        } catch (IOException e) {
            logger.error(ExceptionUtil.getExceptionMessage(e));
        }
        logger.info("WeiXinLoginController ==> getPcWeiXinUserInfo(){} resMessageString: " + resMessageString);
        JSONObject jSONObject = JSON.parseObject(resMessageString);
        return jSONObject;
    }

    /**
     * saveWeixinUser: 创建微信新用户
     */
    @Transactional
    public String saveWeixinUser(Map<String, Object> wxUserInfo) {
        Map<String, Object> param = Maps.newHashMap();
        // map拷贝
        CboxUtils.copyMap(param, wxUserInfo, "nickname,openid,sex,province,city,country,headimgurl,unionid");
        String rec_id = GlobalRecIdUtil.nextRecId();
        param.put("rec_id", rec_id);
        param.put("rec_person", defauleUser);
        param.put("user_id", rec_id);
        param.put("rec_updateperson", defauleUser);
        int count = weixinUserMapper.save(param);
        if (count > 0) {
        }
        return rec_id;
    }

    /**
     * saveSysUser:
     */
    public String saveSysUser(String wxRecId, String phone) {
        // 查询微信用户
        WeixinUser wxUser = this.findById(wxRecId);

        Map<String, Object> param = Maps.newHashMap();
        // param.put("user_id", wxRecId);
        param.put("user_name", phone);
        param.put("nick_name", wxUser.getNickname());
        param.put("user_type", "app");
        param.put("phonenumber", phone);
        param.put("sex", wxUser.getSex());
        param.put("avatar", wxUser.getHeadimgurl());
        String password = "";
        param.put("password", password);
        param.put("status", "5");// 5-未提交资料

        param.put("create_by", defauleUser);
        param.put("create_time", new Date());
        super.saveNoRec("sys_user", param, true);
        Object userId = param.get("rec_id");
        if (userId != null) {
            updateWeixinUser(wxRecId, String.valueOf(userId));
        }
        return String.valueOf(userId);
    }

    /**
     * updateUser：更新微信用户的userId ，更新用户状态为正常状态
     * 
     * @param wxRecId
     * @param userId
     * @return
     */
    public void updateUser(String wxRecId, String userId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("status", "1");// 正常状态
        // 更新条件
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("user_id", userId);
        conditions.put("status", "0");// 待激活状态
        int cot = super.updateNoRec(conditions, "sys_user", param);
        if (cot == 1) {
            updateWeixinUser(wxRecId, userId);
        }
    }

    /**
     * 修改微信用户信息
     * 
     * @param rec_id
     * @param param
     * @return
     */
    public int updateWeixinUser(String rec_id, Map<String, Object> param) {
        param.put("rec_updateperson", defauleUser);
        return this.update(rec_id, wxUserTable, param);
    }

    /**
     * updateWeixinUser: 更新微信用户的 user_id
     * 
     * @param rec_id
     * @param userId
     * @return
     */
    public int updateWeixinUser(String rec_id, String userId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("user_id", userId);
        return this.updateWeixinUser(rec_id, param);
    }

    /**
     * checkPhoneUnique：检查手机号是否已经被绑定
     * 
     * @param phone
     * @return
     */
    public boolean checkPhoneUnique(String phone) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("phonenumber", phone);
        int count = super.count("sys_user", param);
        return count > 0;
    }

    /**
     * getSysUser：获取民兵导入信息
     * 
     * @param phone
     * @return
     */
    public Map<String, Object> getSysUser(String phone) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("phonenumber", phone);
        return weixinUserMapper.getSysUser(param);
    }

}
