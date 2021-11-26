package com.cbox.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.CboxUtils;
import com.cbox.base.utils.id.GlobalRecIdUtil;
import com.cbox.common.AliSmsConstant;

/**
 * 发送短信
 */
@Service
public class AliSendSmsService extends BaseService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AliSmsConfig aliSmsConfig;

    // 设置鉴权参数，初始化客户端
    protected IAcsClient getiAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(aliSmsConfig.getRegionId(), aliSmsConfig.getAccessKeyId(), aliSmsConfig.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }

    public Map<String, String> sendSms(String templateCode, String phoneNumbers, Map<String, String> param) throws ClientException {
        return sendSms(templateCode, phoneNumbers, aliSmsConfig.getSignName(), param);
    }

    /**
     * SendSms 接口是短信发送接口，支持在一次请求中向多个不同的手机号码发送同样内容的短信。
     * 
     * @param templateCode 短信模板
     * @param phoneNumbers 手机号码
     * @param signName 短信签名名称
     * @param param 短信模板参数
     * @return
     * @throws ClientException
     */
    public Map<String, String> sendSms(String templateCode, String phoneNumbers, String signName, Map<String, String> param) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain(aliSmsConfig.getDomain());
        request.setSysVersion(aliSmsConfig.getSysVersion());
        request.setSysAction("SendSms");
        // 接收短信的手机号码
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        // 短信签名名称。请在控制台签名管理页面签名名称一列查看（必须是已添加、并通过审核的短信签名）。
        request.putQueryParameter("SignName", signName);
        // 短信模板ID
        request.putQueryParameter("TemplateCode", templateCode);
        if (param != null && param.size() > 0) {
            // 短信模板变量对应的实际值，JSON格式。
            // 移除map中的空白字符串值
            CboxUtils.removeEmptyKey(param);
            request.putQueryParameter("TemplateParam", JSON.toJSONString(param));
        }
        CommonResponse commonResponse = getiAcsClient().getCommonResponse(request);
        String data = commonResponse.getData();
        String sData = data.replaceAll("'\'", "");
        log_print("sendSms", sData);
        Map<String, String> map = JSON.parseObject(sData, new TypeReference<Map<String, String>>() {
        });

        // 请求状态码。 返回OK代表请求成功。
        String retCode = map.get(AliSmsConstant.RETURN_CODE);
        // 状态码的描述。
        String retMsg = map.get(AliSmsConstant.RETURN_MESSAGE);
        String bizId = map.get("BizId");
        String requestId = map.get("RequestId");

        Map<String, Object> params = new HashMap<String, Object>();
        String rec_id = GlobalRecIdUtil.nextRecId();
        String content = "" + templateCode;
        params.put("phone_numbers", phoneNumbers);
        params.put("content", content);
        params.put("flag", "1");// 已发送
        params.put("code", retCode);
        params.put("message", retMsg);
        // params.put("order_id", order_id);
        params.put("request_id", requestId);
        params.put("biz_id", bizId);
        params.put("rec_id", rec_id);
        params.put("rec_person", "sys");
        params.put("rec_updateperson", "sys");
        this.save("d_sms_log", params);

        if (!AliSmsConstant.OK.equals(retCode)) {
            log.error("发送短信异常！状态码：{}，描述{}", retCode, retMsg);
        }
        return map;
    }

    /**
     * SendBatchSms SendBatchSms接口是短信批量发送接口，支持在一次请求中分别向多个不同的手机号码发送不同签名的短信。 手机号码等参数均为JSON格式，字段个数相同，一一对应，短信服务根据字段在JSON中的顺序判断发往指定手机号码的签名。
     * 
     * @param templateCode
     * @param phoneNumbers
     * @param signName
     * @param param
     * @return
     * @throws ClientException
     */
    public String sendBatchSms(String templateCode, List<String> phoneNumbers, List<String> signName, List<Map<String, String>> param) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain(aliSmsConfig.getDomain());
        request.setSysVersion(aliSmsConfig.getSysVersion());
        request.setSysAction("SendBatchSms");
        // 接收短信的手机号码，JSON数组格式。
        request.putQueryParameter("PhoneNumberJson", JSON.toJSONString(phoneNumbers));
        // 短信签名名称，JSON数组格式。
        request.putQueryParameter("SignNameJson", JSON.toJSONString(signName));
        // 短信模板ID
        request.putQueryParameter("TemplateCode", templateCode);
        if (param != null && param.size() > 0) {
            // 短信模板变量对应的实际值，JSON格式。
            request.putQueryParameter("TemplateParamJson", JSON.toJSONString(param));
        }
        CommonResponse commonResponse = getiAcsClient().getCommonResponse(request);
        String data = commonResponse.getData();
        String sData = data.replaceAll("'\'", "");
        log_print("sendSms", sData);
        Map<String, String> map = JSON.parseObject(sData, new TypeReference<Map<String, String>>() {
        });

        // 请求状态码。 返回OK代表请求成功。
        String retCode = map.get(AliSmsConstant.RETURN_CODE);
        // 状态码的描述。
        String retMsg = map.get(AliSmsConstant.RETURN_MESSAGE);
        if (!AliSmsConstant.OK.equals(retCode)) {
            log.error("发送短信异常！状态码：{}，描述{}", retCode, retMsg);
        }
        String bizId = map.get("BizId");

        return bizId;
    }

    /**
     * 查询发送详情
     * 
     * @param bizId 短信发送回执ID,发送流水号
     * @param phoneNumbers 接收短信的手机号码
     * @param sendDate 短信发送日期，支持查询最近30天的记录。格式为yyyyMMdd，例如20191010。
     * @param pageSize 分页记录数量
     * @param currentPage 分页当前页码
     * @throws ClientException
     */
    public String querySendDetails(String bizId, String phoneNumbers, String sendDate, String pageSize, String currentPage) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain(aliSmsConfig.getDomain());
        request.setSysVersion(aliSmsConfig.getSysVersion());
        request.setSysAction("QuerySendDetails");
        // 接收短信的手机号码
        request.putQueryParameter("PhoneNumber", phoneNumbers);
        // 短信发送日期，支持查询最近30天的记录。格式为yyyyMMdd，例如20191010。
        request.putQueryParameter("SendDate", sendDate);
        // 分页记录数量
        request.putQueryParameter("PageSize", pageSize);
        // 分页当前页码
        request.putQueryParameter("CurrentPage", currentPage);
        // 发送回执ID，即发送流水号。
        request.putQueryParameter("BizId", bizId);
        CommonResponse response = getiAcsClient().getCommonResponse(request);
        log_print("querySendDetails", response.getData());
        return response.getData();
    }

    /**
     * 添加短信模板
     */
    public String addSmsTemplate(String templateName, String templateType, String templateContent, String remark) throws ClientException {
        CommonRequest addSmsTemplateRequest = new CommonRequest();
        addSmsTemplateRequest.setSysDomain(aliSmsConfig.getDomain());
        addSmsTemplateRequest.setSysAction("AddSmsTemplate");
        addSmsTemplateRequest.setSysVersion(aliSmsConfig.getSysVersion());
        // 短信类型。0：验证码；1：短信通知；2：推广短信；3：国际/港澳台消息
        addSmsTemplateRequest.putQueryParameter("TemplateType", templateType);
        // 模板名称，长度为1~30个字符
        addSmsTemplateRequest.putQueryParameter("TemplateName", templateName);
        // 模板内容，长度为1~500个字符
        addSmsTemplateRequest.putQueryParameter("TemplateContent", templateContent);
        // 短信模板申请说明
        addSmsTemplateRequest.putQueryParameter("Remark", remark);
        CommonResponse addSmsTemplateResponse = getiAcsClient().getCommonResponse(addSmsTemplateRequest);
        String data = addSmsTemplateResponse.getData();
        // 消除返回文本中的反转义字符
        String sData = data.replaceAll("'\'", "");
        log_print("addSmsTemplate", sData);
        // 将字符串转换为Map类型，取TemplateCode字段值
        Map<String, String> map = JSON.parseObject(sData, new TypeReference<Map<String, String>>() {
        });
        Object templateCode = map.get("TemplateCode");
        return templateCode.toString();
    }

    private static void log_print(String functionName, Object result) {
        System.out.println("-------------------------------" + functionName + "-------------------------------");
        System.out.println(JSON.toJSON(result));
    }

}