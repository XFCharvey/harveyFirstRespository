package com.cbox.business.email.emailsend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.email.email.mapper.EmailMapper;
import com.cbox.business.email.emailsend.mapper.EmailSendMapper;

/**
 * @ClassName: EmailSendService
 * @Function: 邮件发送详情表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class EmailSendService extends BaseService {

    @Autowired
    private EmailSendMapper emailSendMapper;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private EmailMapper emailMapper;
    

	/** listEmailSend : 获取邮件发送详情表列表数据 **/
	public List<Map<String, Object>> listEmailSend(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = emailSendMapper.listEmailSend(param);

		return list;
	}

    // /** 发送邮件 **/
    // public ResponseBodyVO sendEmail(Map<String, Object> param) {
    // // TODO Auto-generated method stub
    // int count = 0;
    // // 邮件消息对象
    // SimpleMailMessage message = new SimpleMailMessage();
    // // 收件人邮箱的id
    // String email_id = StrUtil.getMapValue(param, "email_id");
    //
    // // 查询除对应的邮箱账号
    // Map<String, Object> mapEmailCondition = new HashMap<String, Object>();
    // mapEmailCondition.put("rec_id", email_id);
    // Map<String, Object> mapEmail = emailMapper.getEmail(param);
    // if (ObjUtil.isNotNull(mapEmail)) {
    // // 和配置文件中的的username相同，相当于发送方
    // message.setFrom("a15559539150@163.com");
    // // 收件人邮箱
    // message.setTo(StrUtil.getMapValue(mapEmail, "email_url"));
    // // 标题
    // message.setSubject(StrUtil.getMapValue(param, "email_title"));
    // // 正文内容
    // message.setText(StrUtil.getMapValue(param, "email_content"));
    // // 发送
    // mailSender.send(message);
    // }
    //
    // return ServerRspUtil.formRspBodyVO(count, "删除邮件表失败");
    //
    // }

    public List<Map<String, Object>> countMailBySend(Map<String, Object> param) {
        List<Map<String, Object>> list = emailSendMapper.countMailBySend(param);

        return list;
    }

    public List<Map<String, Object>> countMailByResv(Map<String, Object> param) {
        List<Map<String, Object>> list = emailSendMapper.countMailByResv(param);

        return list;
    }

    // /** 获取抄送人包含当前用户的邮件 **/
    // public List<Map<String, Object>> listEmailCopyPerson(Map<String, Object> param) {
    // // TODO Auto-generated method stub
    // super.appendUserInfo(param);
    // String userName = SecurityUtils.getUsername();
    //
    // List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
    //
    // List<Map<String, Object>> listEmail = emailSendMapper.listEmailSend(param);
    // for (int i = 0; i < listEmail.size(); i++) {
    // Map<String, Object> mapEamil = listEmail.get(i);
    // String copyPerson = StrUtil.getMapValue(mapEamil, "email_copy");
    // List<String> copyPersonList = Arrays.asList(copyPerson.split(","));
    // if (copyPersonList.contains(userName)) {
    // listResult.add(mapEamil);
    // }
    // }
    // return listResult;
    // }

	/** getEmailSend : 获取指定id的邮件发送详情表数据 **/
	public ResponseBodyVO getEmailSend(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = emailSendMapper.getEmailSend(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addEmailSend : 新增邮件发送详情表 **/
	public ResponseBodyVO addEmailSend(Map<String, Object> param) {

        String userName = SecurityUtils.getUsername();
        String nowTime = DateUtils.getTime();
		// Table:d_email_send
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("email_id", param.get("email_id"));
        mapParam.put("email_copy", param.get("email_copy"));
		mapParam.put("email_title", param.get("email_title"));
        mapParam.put("send_time", nowTime);
        mapParam.put("send_person", userName);
		mapParam.put("email_content", param.get("email_content"));
		mapParam.put("email_img", param.get("email_img"));
		mapParam.put("emai_video", param.get("emai_video"));
		mapParam.put("email_file", param.get("email_file"));
        mapParam.put("send_status", "1");
		int count = this.save( "d_email_send", mapParam);

        // 邮件消息对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 收件人邮箱的id
        String email_id = StrUtil.getMapValue(param, "email_id");

        // 查询除对应的邮箱账号
        Map<String, Object> mapEmailCondition = new HashMap<String, Object>();
        mapEmailCondition.put("rec_id", email_id);
        Map<String, Object> mapEmail = emailMapper.getEmail(mapEmailCondition);
        if (ObjUtil.isNotNull(mapEmail)) {
            // 和配置文件中的的username相同，相当于发送方
            message.setFrom("rongkac@163.com");
            // 收件人邮箱
            message.setTo(StrUtil.getMapValue(mapEmail, "email_url"));
            // 标题
            message.setSubject(StrUtil.getMapValue(param, "email_title"));
            // 正文内容
            message.setText(StrUtil.getMapValue(param, "email_content"));
            // 发送
            mailSender.send(message);
        }


		return ServerRspUtil.formRspBodyVO(count, "新增邮件发送详情表失败");
	}

	/** updateEmailSend : 修改邮件发送详情表 **/
	public ResponseBodyVO updateEmailSend(Map<String, Object> param) {

		// Table:d_email_send
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("email_id", param.get("email_id"));
        mapParam.put("email_copy", param.get("email_copy"));
		mapParam.put("email_title", param.get("email_title"));
		mapParam.put("send_time", param.get("send_time"));
        mapParam.put("send_person", param.get("send_person"));
		mapParam.put("email_content", param.get("email_content"));
		mapParam.put("email_img", param.get("email_img"));
		mapParam.put("emai_video", param.get("emai_video"));
		mapParam.put("email_file", param.get("email_file"));
        mapParam.put("send_status", param.get("send_status"));
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_email_send", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改邮件发送详情表失败"); 
	}

	/** delEmailSend : 删除邮件发送详情表 **/
	public ResponseBodyVO delEmailSend(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_email_send
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_email_send",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除邮件发送详情表失败");
	}


}
