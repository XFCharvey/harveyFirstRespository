package com.cbox.business.user.messagenotice.service;

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
import com.cbox.business.user.messagenotice.mapper.MessageNoticeMapper;


/**
 * @ClassName: MessageNoticeService
 * @Function: 消息通知
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class MessageNoticeService extends BaseService {

    @Autowired
    private MessageNoticeMapper messageNoticeMapper;

	/** listMessageNotice : 获取消息通知列表数据 **/
    public List<Map<String, Object>> listMessageNotice(Map<String, Object> param) {
		super.appendUserInfo(param);
        List<Map<String, Object>> list = messageNoticeMapper.listMessageNotice(param);

        return list;
	}

	/** getMessageNotice : 获取指定id的消息通知数据 **/
	public ResponseBodyVO getMessageNotice(Map<String, Object> param) {
		super.appendUserInfo(param);

        Map<String, Object> mapResult = messageNoticeMapper.getMessageNotice(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addMessageNotice : 新增消息通知 **/
	public ResponseBodyVO addMessageNotice(Map<String, Object> param) {

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
    public ResponseBodyVO updateMessageNotice(Map<String, Object> param) {

        // Table:d_customer_notice
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("notice_status", "done");
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.updateNoRec(mapCondition, "d_customer_notice", mapParam);

        return ServerRspUtil.formRspBodyVO(count, "修改消息通知失败");
    }

	/** delMessageNotice : 删除消息通知 **/
	public ResponseBodyVO delMessageNotice(Map<String, Object> param) {

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
        int count = messageNoticeMapper.allDoneNotice(param);

        return ServerRspUtil.formRspBodyVO(count, "修改消息通知失败");
    }


}
