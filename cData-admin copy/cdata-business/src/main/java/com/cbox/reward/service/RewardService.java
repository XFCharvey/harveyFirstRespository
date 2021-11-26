package com.cbox.reward.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.aliyuncs.exceptions.ClientException;
import com.beust.jcommander.internal.Maps;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.common.AliSmsConstant;
import com.cbox.common.service.AliSendSmsService;
import com.cbox.reward.mapper.RewardMapper;
import com.google.common.collect.ImmutableMap;

@Service
@Transactional
public class RewardService extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RewardMapper mapper;
    @Autowired
    private AliSendSmsService aliSendSmsService;

    /** 检查是否已经抽奖过 */
    public ResponseBodyVO check(String phone, String deviceid) {
        Map<String, String> param = ImmutableMap.of("qbank_id", "170", "phone", phone, "deviceid", deviceid);
        Map<String, Object> data = mapper.getExamRecord(param);
        Map<String, Object> result = Maps.newHashMap();
        String code = "0";
        if (!CollectionUtils.isEmpty(data)) {
            code = "1";// 已提交、已抽奖
            result.put("phone", data.get("person_phone"));
            result.put("recId", data.get("rec_id"));
            if (StrUtil.isNull(data.get("reward_id"))) {
                code = "2";// 已提交、未抽奖
            } else {
                if ("8".equals(StrUtil.getMapValue(data, "reward_id"))) {// 谢谢参与
                    result.put("reward_name", "0");
                } else {
                    result.put("reward_name", data.get("reward_name"));
                }
            }
        }
        result.put("code", code);
        return ServerRspUtil.success(result);
    }

    public Map<String, Object> getExamRecordByPhone(String phone) {
        Map<String, String> param = ImmutableMap.of("qbank_id", "170", "phone", phone);
        Map<String, Object> data = mapper.getExamRecordByPhone(param);
        return data;
    }

    /** 检查手机号是否已抽奖 */
    public boolean checkPhoneReward(String phone) {
        Map<String, Object> data = getExamRecordByPhone(phone);
        return !CollectionUtils.isEmpty(data) && !"".equals(StrUtil.getMapValue(data, "reward_id"));
    }

    /** 检查手机号是否已提交 */
    public boolean checkPhone(String phone) {
        Map<String, Object> data = getExamRecordByPhone(phone);
        return !CollectionUtils.isEmpty(data);
    }

    /** 获取已抽奖总数 */
    public int countReward(String qbank_id) {
        return mapper.countReward(qbank_id);
    }

    /**
     * * 中奖纪录保存
     * 
     * @param param
     * @return
     */
    public ResponseBodyVO save(Map<String, Object> param) {
        Map<String, Object> mapParam = Maps.newHashMap();
        mapParam.put("reward_id", param.get("prize_id"));
        mapParam.put("rec_id", param.get("recId"));
        int count = mapper.updateExamRecordReward(mapParam);
        return ServerRspUtil.success(count);
    }

    public ResponseBodyVO saveExam(Map<String, Object> param) {
        if (checkPhone((String) param.get("phone"))) {
            return ServerRspUtil.error("该手机号已参加过抽奖，请更换");
        }

        String nowTime = DateUtils.getTime();
        Map<String, Object> mapRecordParam = Maps.newHashMap();
        mapRecordParam.put("qbank_id", param.get("qbank_id"));
        mapRecordParam.put("exam_person", param.get("name"));
        mapRecordParam.put("exam_time", nowTime);
        mapRecordParam.put("record_type", "ques");//
        mapRecordParam.put("use_time", param.get("use_time"));
        mapRecordParam.put("person_phone", param.get("phone"));
        mapRecordParam.put("person_deviceid", param.get("deviceid"));
        mapRecordParam.put("person_work", param.get("work"));
        mapRecordParam.put("person_birthyear", param.get("birthyear"));
        mapRecordParam.put("person_sex", param.get("sex"));
        mapRecordParam.put("question_groupids", param.get("groupids"));
        mapRecordParam.put("last_question_groupid", param.get("last_groupid"));
        int count = this.saveNoRec("d_exam_record", mapRecordParam, true);
        if (count > 0) {
            String recId = StrUtil.getMapValue(mapRecordParam, "rec_id");
            Map<String, String> titles = (Map<String, String>) param.get("titles");
            for (Map.Entry<String, String> entry : titles.entrySet()) {
                Map<String, Object> mapParamdetail = Maps.newHashMap();
                String k = entry.getKey();
                String v = entry.getValue();
                mapParamdetail.put("title_id", k);
                mapParamdetail.put("answers", v);
                mapParamdetail.put("qbank_id", param.get("qbank_id"));
                mapParamdetail.put("record_id", recId);
                this.saveNoRec("d_exam_record_detail", mapParamdetail, false);
            }
            return ServerRspUtil.success(recId);
        }
        return ServerRspUtil.error("问卷提交失败");
    }

    /**
     * @return
     * @throws ClientException
     */
    public int sendSms() throws ClientException {
        // 奖品短信模板，X元话费模板
        Map<Integer, String> rewardTempMap = ImmutableMap.of(1, "SMS_225995064", 2, "SMS_225980074", 3, "SMS_225995066", 4, "SMS_225980076", 5, "SMS_225990088");
        Map<String, Object> params = Maps.newHashMap();
        params.put("send_sms", "0");
        List<Map<String, Object>> list = super.queryNoRec("d_exam_record_cash", params);
        logger.info("需发送短信数量{}", list.size());
        int num = 0;
        for (Map<String, Object> data : list) {
            int rewardId = StrUtil.getMapIntValue(data, "reward_id");
            String phone = StrUtil.getMapValue(data, "person_phone");

            // 短信模板SMS_225980009:话费、
            // SMS_225995004：爱奇艺 reward_id=6 、
            // SMS_225990007：优酷 reward_id=7
            String template = "";// 话费
            Map<String, String> smsParam = Maps.newHashMap();
            if (rewardId == 6) {
                template = "SMS_225995004";
            } else if (rewardId == 7) {
                template = "SMS_225990007";
            } else {
                template = rewardTempMap.get(rewardId);// X元话费
            }
            if (StrUtil.isNotNull(template)) {
                // 发送短信验证码
                Map<String, String> map = aliSendSmsService.sendSms(template, phone, smsParam);
                // 请求状态码。 返回OK代表请求成功。
                String retCode = map.get(AliSmsConstant.RETURN_CODE);
                if (AliSmsConstant.OK.equals(retCode)) {
                    Map<String, Object> params1 = Maps.newHashMap();
                    params1.put("send_sms", "1");
                    Map<String, Object> conditions = Maps.newHashMap();
                    conditions.put("person_phone", phone);
                    conditions.put("send_sms", "0");
                    super.updateNoRec(conditions, "d_exam_record_cash", params1);
                    num++;
                } else {
                    // 状态码的描述。
                    String retMsg = map.get(AliSmsConstant.RETURN_MESSAGE);
                    logger.error("发送短信异常！状态码：{}，描述{}", retCode, retMsg);
                    break;
                }
            } else {
                logger.error("奖品{}未获取到短信模板" + rewardId);
            }
        }
        return num;
    }

}
