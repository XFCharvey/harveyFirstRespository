package com.cbox.push;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.app.appinfo.mapper.AppInfoMapper;

@Service
public class PersonalPushService {

    @Autowired
    PushUtil pushUtil;

    @Autowired
    private AppInfoMapper appInfoMapper;

    public ResponseBodyVO send(@RequestBody Map<String, Object> param) {
        System.out.println(param);
        String pushType = StrUtil.getMapValue(param, "push_type");
        String pushPerson = StrUtil.getMapValue(param, "push_person");
        String pushValue = StrUtil.getMapValue(param, "push_value");
        String pushTitle = StrUtil.getMapValue(param, "push_title");
        //
        Map<String,Object> mapAppInfoCondition = new HashMap<String, Object>();
        mapAppInfoCondition.put("user_name", pushPerson);
        
        Map<String, Object> mapAppInfo = appInfoMapper.getAppInfo(mapAppInfoCondition);
        
        String clientId = StrUtil.getMapValue(mapAppInfo, "clientid");
        String osname = StrUtil.getMapValue(mapAppInfo, "osname");
        JSONObject pay = new JSONObject();
        pay.put("type", pushType);
        pay.put("value", pushValue);
        pushUtil.send(osname, pushTitle, pushValue, clientId, pay);
        return ServerRspUtil.success(param);
    }
}
