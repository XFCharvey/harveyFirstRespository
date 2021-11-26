package com.cbox.business.customer.customernotice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.customer.customerhouses.mapper.CustomerHousesMapper;
import com.cbox.business.customer.customernotice.mapper.CustomerNoticeMapper;
import com.cbox.business.project.projecthouses.mapper.ProjectHousesMapper;


/**
 * @ClassName: MessageNoticeService
 * @Function: 消息通知
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CustomerNoticeService extends BaseService {

    @Autowired
    private CustomerNoticeMapper customerNoticeMapper;
    @Autowired
    private ProjectHousesMapper projectHousesMapper;

    @Autowired
    private CustomerHousesMapper customerHousesMapper;

    /** listCustomerNotice : 获取消息通知列表数据 **/
    public List<Map<String, Object>> listCustomerNotice(Map<String, Object> param) {
		super.appendUserInfo(param);
        List<Map<String, Object>> list = customerNoticeMapper.listCustomerNotice(param);

        return list;
	}

    /** listUserCustomerNotice : 列出本人作为置业顾问，所关联的客户尚未过期的相关提醒 **/
    public List<Map<String, Object>> listUserCustomerNotice(Map<String, Object> param) {
        // TODO Auto-generated method stub
        String userName = SecurityUtils.getUsername();
        Map<String, Object> mapHousesCondition = new HashMap<String, Object>();
        mapHousesCondition.put("counselor", userName);

        List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();

        List<Map<String, Object>> listHouses = projectHousesMapper.listProjectHouses(mapHousesCondition);

        List<Map<String, Object>> listAllCustomerHouses = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listHouses.size(); i++) {
            Map<String, Object> mapHouses = listHouses.get(i);
            // 取出对应的房间ID
            String houseID = StrUtil.getMapValue(mapHouses, "rec_id");
            Map<String, Object> mapCustomerHousesCondition = new HashMap<String, Object>();
            mapCustomerHousesCondition.put("house_id", houseID);
            // 查询出所有该房间的所有关联的用户.
            List<Map<String, Object>> listCustomerHouses = customerHousesMapper.listCustomerHouses(mapCustomerHousesCondition);
            listAllCustomerHouses.addAll(listCustomerHouses);
            // // 查询
            // Map<String, Object> mapCustomerNoticeCondition = new HashMap<String, Object>();
            // mapCustomerNoticeCondition.put("notice_person", customerID);

            // TODO 通过通知人作为条件全找出来
            // List<Map<String, Object>> listNotice = customerNoticeMapper.listOverdueMessageNotice(mapCustomerNoticeCondition);
            // listResult.addAll(listNotice);
        }

        // 去掉最后的出来的所有客户房源关联数据里重复的客户的数据
        if (ObjUtil.isNotNull(listAllCustomerHouses)) {
            listAllCustomerHouses = ObjUtil.removeRepeatMapByKey(listAllCustomerHouses, "customer_id");
            for (int i = 0; i < listAllCustomerHouses.size(); i++) {
                Map<String, Object> mapAllCustomerHouses = listAllCustomerHouses.get(i);
                String customerID = StrUtil.getMapValue(mapAllCustomerHouses, "customer_id");
                // // 查询
                Map<String, Object> mapCustomerNoticeCondition = new HashMap<String, Object>();
                mapCustomerNoticeCondition.put("notice_person", customerID);

                // TODO 通过通知人作为条件全找出来
                List<Map<String, Object>> listNotice = customerNoticeMapper.listOverdueCustomerNotice(mapCustomerNoticeCondition);
                listResult.addAll(listNotice);
            }
        }
        // 将listResult最终数据按完成期限差距大小升序排序
        if (ObjUtil.isNotNull(listResult) && listResult.size() > 1) {
            Collections.sort(listResult, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Integer o1Value = Integer.valueOf(o1.get("date_disparity").toString());
                    Integer o2Value = Integer.valueOf(o2.get("date_disparity").toString());
                    return o1Value.compareTo(o2Value);
                }
            });
        }
        return listResult;
    }


	/** getMessageNotice : 获取指定id的消息通知数据 **/
    public ResponseBodyVO getCustomerNotice(Map<String, Object> param) {
		super.appendUserInfo(param);

        Map<String, Object> mapResult = customerNoticeMapper.getCustomerNotice(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addMessageNotice : 新增消息通知 **/
    public ResponseBodyVO addCustomerNotice(Map<String, Object> param) {

		// Table:d_customer_notice
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("notice_title", param.get("notice_title"));
		mapParam.put("notice_type", param.get("notice_type"));
        mapParam.put("relation_id", param.get("relation_id"));
		mapParam.put("notice_content", param.get("notice_content"));
		mapParam.put("notice_person", param.get("notice_person"));
        mapParam.put("notice_time", param.get("notice_time"));
        int count = this.saveNoRec("d_customer_notice", mapParam, false);

		return ServerRspUtil.formRspBodyVO(count, "新增消息通知失败");
	}

	/** updateMessageNotice : 修改消息通知 **/
    public ResponseBodyVO updateCustomerNotice(Map<String, Object> param) {

        // Table:d_customer_notice
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("notice_status", "done");
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.updateNoRec(mapCondition, "d_customer_notice", mapParam);

        return ServerRspUtil.formRspBodyVO(count, "修改消息通知失败");
    }

	/** delMessageNotice : 删除消息通知 **/
    public ResponseBodyVO delCustomerNotice(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_customer_notice
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.deleteEmpty("d_customer_notice", mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除消息通知失败");
	}

    public ResponseBodyVO allDoneNotice(Map<String, Object> param) {
        // TODO Auto-generated method stub
        // Table:d_customer_notice
        int count = customerNoticeMapper.allDoneNotice(param);

        return ServerRspUtil.formRspBodyVO(count, "修改消息通知失败");
    }


}
