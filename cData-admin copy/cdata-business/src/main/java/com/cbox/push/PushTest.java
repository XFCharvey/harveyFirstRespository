package com.cbox.push;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.Settings;
import com.getui.push.v2.sdk.dto.req.Strategy;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;

public class PushTest {

    private static String packageName = "uni.UNIBBC2C49";
    private static String appId = "VFupW9rLov8U7cm9hvxZm6";
    private static String AppKey = "qltJgFaJRV9rAJnstYqWG4";
    private static String MasterSecret = "77DEPrtvzJAcUfDd4Rq0M2";

    public static void main(String[] args) {
        String clientId = "7fa143e83bcd64b7dcc131cb563843c7";
        String title = "测试ti888";
        String content = "消息内容测试888";
        JSONObject pay = new JSONObject();
        pay.put("type", "1");
        pay.put("value", "888");
        String payload = JSON.toJSONString(pay);

        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        // 填写应用配置
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(AppKey);
        apiConfiguration.setMasterSecret(MasterSecret);
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        // 根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");// requestid需要每次变化唯一
        // 配置推送条件
        // 1: 表示该消息在用户在线时推送个推通道，用户离线时推送厂商通道;
        // 2: 表示该消息只通过厂商通道策略下发，不考虑用户是否在线;
        // 3: 表示该消息只通过个推通道下发，不考虑用户是否在线；
        // 4: 表示该消息优先从厂商通道下发，若消息内容在厂商通道代发失败后会从个推通道下发。
        Strategy strategy = new Strategy();
        strategy.setDef(1);
        Settings settings = new Settings();
        settings.setStrategy(strategy);
        pushDTO.setSettings(settings);
        settings.setTtl(3600000);// 消息有效期，走厂商消息需要设置该值
        // 推送苹果离线通知标题内容
        Alert alert = new Alert();
        alert.setTitle("苹果离线" + title);
        alert.setBody("苹果离线" + content);
        Aps aps = new Aps();
        // 1表示静默推送(无通知栏消息)，静默推送时不需要填写其他参数。
        // 苹果建议1小时最多推送3条静默消息
        aps.setContentAvailable(0);
        aps.setSound("default");
        aps.setAlert(alert);
        IosDTO iosDTO = new IosDTO();
        iosDTO.setAps(aps);
        iosDTO.setType("notify");
        PushChannel pushChannel = new PushChannel();
        pushChannel.setIos(iosDTO);
        // 安卓离线厂商通道推送消息体
        // PushChannel pushChannel = new PushChannel();
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();
        ThirdNotification notification1 = new ThirdNotification();

        ups.setNotification(notification1);
        notification1.setTitle("安卓离线" + title);
        notification1.setBody("安卓离线" + content);
        notification1.setClickType("intent");
        notification1.setIntent("intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;component=" + packageName + "/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title=" + title
                + ";S.content=" + content + ";S.payload=" + payload + ";end");
        // 各厂商自有功能单项设置
        // ups.addOption("HW", "/message/android/notification/badge/class", "io.dcloud.PandoraEntry ");
        // ups.addOption("HW", "/message/android/notification/badge/add_num", 1);
        // ups.addOption("HW", "/message/android/notification/importance", "HIGH");
        // ups.addOption("VV","classification",1);
        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);
        pushDTO.setPushChannel(pushChannel);

        // PushMessage在线走个推通道才会起作用的消息体
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        pushMessage.setTransmission("{title:\"" + title + "\",content:\"" + content + "\",payload:\"" + payload + "\"}");
        // 设置接收人信息
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.addCid(clientId);

        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) {
            // success
            System.out.println(apiResult.getData());
        } else {
            // failed
            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }
}