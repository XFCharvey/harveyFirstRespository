package com.cbox.reward.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RewardMapper {

    public Map<String, Object> getExamRecord(Map<String, String> param);

    public Map<String, Object> getExamRecordByPhone(Map<String, String> param);

    public int countReward(@Param("qbank_id") String qbank_id);
    
    public int updateExamRecordReward(Map<String, Object> param);

}
