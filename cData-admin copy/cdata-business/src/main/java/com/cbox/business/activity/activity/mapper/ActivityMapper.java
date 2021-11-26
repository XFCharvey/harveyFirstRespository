package com.cbox.business.activity.activity.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityMapper {

    /** listCommodity : 获取线下活动表列表数据 **/
    List<Map<String, Object>> listActivity(Map<String, Object> param);

    /** getActivity : 获取指定id的数据 **/
    Map<String, Object> getActivity(Map<String, Object> param);

    Map<String, Object> countinvitationNum(Object object);

    List<Map<String, Object>> invitationRange(int deptid);

    List<Map<String, Object>> filterActivity(Map<String, Object> param);

    /** 获取30天内进行中的活动,按活动时间、部门级别排序 **/
    List<Map<String, Object>> listActivityRecently30(Map<String, Object> param);

    List<Map<String, Object>> listLiveActivity(Map<String, Object> param);
    
}
