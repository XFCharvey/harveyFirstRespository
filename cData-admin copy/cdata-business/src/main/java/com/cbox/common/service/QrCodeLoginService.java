package com.cbox.common.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.domain.entity.SysUser;
import com.cbox.base.core.domain.model.LoginUser;
import com.cbox.base.core.redis.RedisCache;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ExceptionUtil;
import com.cbox.base.utils.QRCodeGenerator;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.base.utils.sign.Base64;
import com.cbox.base.utils.uuid.IdUtils;
import com.cbox.framework.web.service.AuthLevelService;
import com.cbox.framework.web.service.SysPermissionService;
import com.cbox.framework.web.service.TokenService;
import com.cbox.system.service.ISysUserService;
import com.google.common.collect.Maps;
import com.google.zxing.WriterException;

@Component
public class QrCodeLoginService extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int expireMinute = 5;// 过期时间 5分钟

    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private TokenService tokenService;
    // @Autowired
    // private DeptAdminService deptAdminService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthLevelService authLevelService;

    /**
     * genQrCode： 生成二维码
     * 
     * @param type 二维码类型 0:登录 1:绑定管理员
     * @return
     */
    public AjaxResult genQrCode(String type) {
        // 验证码信息
        String uuid = IdUtils.simpleUUID();
        // 转换流信息写出
        FastByteArrayOutputStream stream = new FastByteArrayOutputStream();
        try {
            String content = uuid + "-" + type;// 二维码内容
            QRCodeGenerator.generateQRCodeImage(content, stream);
            this.saveQrCode(uuid, type);
            redisCache.setCacheObject(uuid, "0", expireMinute, TimeUnit.MINUTES);// 存redis
        } catch (IOException | WriterException e) {
            logger.error(ExceptionUtil.getExceptionMessage(e));
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(stream.toByteArray()));
        return ajax;
    }

    /**
     * saveQrCode 保存二维码信息
     * 
     * @param uuid
     * @param type
     * @return
     */
    public int saveQrCode(String uuid, String type) {
        Map<String, Object> params = Maps.newHashMap();
        Date date = new Date();
        Date expire_time = DateUtils.addMinutes(date, expireMinute);// 过期时间 5分钟
        params.put("uuid", uuid);
        params.put("type", type);
        params.put("rec_status", "1");
        params.put("expire_time", expire_time);
        params.put("rec_time", date);
        return super.saveNoRec("sys_qr_code", params);
    }

    enum QrCode {
        TODO(0, "待扫码"), NO_UUID(-1, "请求的uuid不存在"), OK(1, "用户确认"), CANCEL(2, "用户已取消"), EXPIRE(3, "二维码已过期"), OTHER(9, "其他错误");

        private int code;
        private String name;

        // 构造方法
        private QrCode(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * check 校验
     * 
     * @param uuid
     * @return
     */
    public ResponseBodyVO checkQrCode(String uuid) {
        // 二维码校验结果
        QrCode qrc = QrCode.TODO;// 默认值、待扫码
        // 登录成功，返回多个信息
        Map<String, Object> result = new HashMap<String, Object>();

        // 从redis获取扫码结果
        String value = redisCache.getCacheObject(uuid);
        // System.out.println("redis:" + value);
        if (StringUtils.isEmpty(value)) {
            qrc = QrCode.EXPIRE;
        } else {
            if ("1".equals(value)) {// 确认操作
                redisCache.deleteObject(uuid);// 删除redis key
                Map<String, Object> params = Maps.newHashMap();
                params.put("uuid", uuid);
                Map<String, Object> map = super.queryOne("sys_qr_code", params);
                // 二维码类型 0:登录 1:绑定管理员
                String type = String.valueOf(map.get("type"));
                String userid = StrUtil.getMapValue(map, "scan_userid");
                if ("0".equals(type)) {// 登录类型 登录，返回token等信息
                    SysUser user = userService.selectUserById(Long.parseLong(userid));
                    authLevelService.addUserLevelDeptAuth(user);
                    LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
                    // 生成token
                    result.put("token", tokenService.createToken(loginUser));
                    result.put("user", loginUser.getUser());
                    qrc = QrCode.OK;
                } else if ("1".equals(type)) {// 确认 绑定管理员
                    // ResponseBodyVO res = deptAdminService.bindAdmin("", userid);
                    // if (res.isSuccess()) {
                    // qrc = QrCode.OK;
                    // } else {
                    // qrc = QrCode.OTHER;
                    // result.put("msg", res.getMsg());
                    // }
                }
            } else if ("2".equals(value)) {// 取消操作
                redisCache.deleteObject(uuid);// 删除redis key
                qrc = QrCode.CANCEL;
            }
        }

        result.put("code", qrc.getCode());
        if (!result.containsKey("msg")) {
            result.put("msg", qrc.getName());
        }
        return ServerRspUtil.success(result);
    }

    public ResponseBodyVO submitQrCode(String uuid, String result) {
        Map<String, Object> params = Maps.newHashMap();
        long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        params.put("scan_userid", userId);
        params.put("scan_result", result);
        params.put("scan_time", new Date());
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("uuid", uuid);
        conditions.put("scan_result", "0");// 待扫码
        int cot = super.updateNoRec(conditions, "sys_qr_code", params);
        if (cot > 0) {
            // 扫码结果更新redis结果，过期时间1分钟即可
            redisCache.setCacheObject(uuid, result, 1, TimeUnit.MINUTES);
            ServerRspUtil.success("success", "");
        }
        return ServerRspUtil.success("二维码已过期", "");
    }

}
