package com.cbox.business.timetask.mock.util;

import java.util.Map;

import com.cbox.base.utils.DateUtils;

public class TimeUtil {

    public static void appendRecs(Map<String, Object> mapParam) {
        mapParam.put("rec_status", "1");
        mapParam.put("rec_person", "sys");
        mapParam.put("rec_updateperson", "sys");
        mapParam.put("rec_time", DateUtils.dateTimeNow());
        mapParam.put("rec_updatetime", DateUtils.dateTimeNow());
    }

}
