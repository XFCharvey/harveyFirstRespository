package com.cbox.business.check.checkinfo.service;

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
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.ObjUtil;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.activity.activity.mapper.ActivityMapper;
import com.cbox.business.check.checkinfo.mapper.CheckInfoMapper;
import com.cbox.business.info.infocontent.mapper.InfoContentMapper;
import com.cbox.business.sysDept.mapper.DeptMapper;
import com.cbox.business.user.sysUser.mapper.UserMapper;
import com.cbox.common.service.AliSendSmsService;
import com.cbox.push.PersonalPushService;


/**
 * @ClassName: CheckInfoService
 * @Function: 审核信息表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class CheckInfoService extends BaseService {

    @Autowired
    private CheckInfoMapper checkInfoMapper;


    @Autowired
    private AliSendSmsService aliSendSmsService;
    
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InfoContentMapper infoContentMapper;


    @Autowired
    PersonalPushService personalPushService;

	/** listCheckInfo : 获取审核信息表列表数据 **/
	public List<Map<String, Object>> listCheckInfo(Map<String, Object> param) {
		super.appendUserInfo(param);

		List<Map<String, Object>> list = checkInfoMapper.listCheckInfo(param);

		return list;
	}

	/** getCheckInfo : 获取指定id的审核信息表数据 **/
	public ResponseBodyVO getCheckInfo(Map<String, Object> param) {
		super.appendUserInfo(param);

		Map<String, Object> mapResult = checkInfoMapper.getCheckInfo(param);

		return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
	}

	/** addCheckInfo : 新增审核信息表 **/
	public ResponseBodyVO addCheckInfo(Map<String, Object> param) {

		// Table:d_check_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("dept_id", param.get("dept_id"));
		mapParam.put("check_type", param.get("check_type"));
		mapParam.put("relation_id", param.get("relation_id"));
		mapParam.put("check_title", param.get("check_title"));
        mapParam.put("publish_time", param.get("public_time"));
		mapParam.put("check_desc", param.get("check_desc"));
		mapParam.put("check_status", param.get("check_status"));
		mapParam.put("check_reject_reason", param.get("check_reject_reason"));
        mapParam.put("check_time", param.get("check_time"));
        mapParam.put("check_person", param.get("check_person"));
		int count = this.save( "d_check_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "新增审核信息表失败");
	}

	/** updateCheckInfo : 修改审核信息表 **/
	public ResponseBodyVO updateCheckInfo(Map<String, Object> param) {

		// Table:d_check_info
		Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("dept_id", param.get("dept_id"));
        mapParam.put("check_type", param.get("check_type"));
        mapParam.put("relation_id", param.get("relation_id"));
        mapParam.put("check_title", param.get("check_title"));
        mapParam.put("publish_time", param.get("public_time"));
        mapParam.put("check_desc", param.get("check_desc"));
        mapParam.put("check_status", param.get("check_status"));
        mapParam.put("check_reject_reason", param.get("check_reject_reason"));
        mapParam.put("check_time", param.get("check_time"));
        mapParam.put("check_person", param.get("check_person"));

		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.update(mapCondition, "d_check_info", mapParam);

		return ServerRspUtil.formRspBodyVO(count, "修改审核信息表失败"); 
	}

    /** updateCheckInfo : 修改审核信息表 **/
    public ResponseBodyVO checkStatu(List<Map<String, Object>> param) {
        int count = 0;
        String nowtime = DateUtils.getTime();
        String loginUser = SecurityUtils.getUsername();
        List<Map<String, Object>> paramList = param;
        for (int i = 0; i < paramList.size(); i++) {
            Map<String, Object> mapCheck = paramList.get(i);
            Map<String, Object> checkCondition = new HashMap<String, Object>();
            checkCondition.put("rec_id", mapCheck.get("rec_id"));
            Map<String, Object> mapCheckInfo = checkInfoMapper.getCheckInfo(checkCondition);
            if (ObjUtil.isNull(mapCheckInfo)) {
                return ServerRspUtil.formRspBodyVO(count, "该信息已删除，请刷新确认");
            } else {
                Map<String, Object> checkParams = new HashMap<String, Object>();
                checkParams.put("check_time", nowtime);
                checkParams.put("check_reject_reason", mapCheck.get("check_reject_reason"));
                checkParams.put("check_person", loginUser);
                checkParams.put("check_status", mapCheck.get("check_status"));
                count = this.update(checkCondition, "d_check_info", checkParams);
                
                String type = StrUtil.getMapValue(mapCheck, "check_type");
                if(type.equals("info")) {
                	checkInfoContent(mapCheck);
                }else if(type.equals("act")) {
                	checkActContent(mapCheck);
                }else {
                	System.out.println("暂不支持此类型");
                }
                
//                // 更新对应的消息通知的状态与处理人
//                Map<String, Object> noticeCondition = new HashMap<String, Object>();
//                noticeCondition.put("relation_id", mapCheck.get("rec_id"));
//                noticeCondition.put("notice_type", "check");
//
//                Map<String, Object> noticeParam = new HashMap<String, Object>();
//                noticeParam.put("notice_status", "done");
//                noticeParam.put("notice_handler", loginUser);
//
//                this.updateNoRec(noticeCondition, "d_notice", noticeParam);

//                switch (type) {
//                case "info":
//                    checkInfoContent(mapCheck);
//                    break;
//                case "act":
//                    checkActContent(mapCheck);
//                    break;
                // case "user":
                // checkUserContent(mapCheck);
                // break;
                // case "comment":
                // checkCommentContent(mapCheck);
                // break;
                // case "family":
                // checkfamilyContent(mapCheck);
                // break;
                // case "dept":
                // checkdeptContent(mapCheck);
                // break;
                // case "deptstop":
                // checkdeptStopContent(mapCheck);
                // break;
                // case "accusation":
                // checkAccusationContent(mapCheck);
                // break;
//                default:
//                    System.out.println("暂不支持此类型");
//                }
            }
        }
        return ServerRspUtil.formRspBodyVO(count, "修改审核信息表失败");

    }

    // private boolean checkAccusationContent(Map<String, Object> param) {
    // int count = 0;
    //
    // String loginUser = SecurityUtils.getUsername();
    // // 获取当前时间字符串
    // String nowtime = DateUtils.getTime();
    // // 获取参数里的关联ID作为举报信息的recID
    // String accId = StrUtil.getMapValue(param, "relation_id");
    // // 获取审核类型
    // String check_status = StrUtil.getMapValue(param, "check_status");
    //
    // Map<String, Object> mapAccusationCondition = new HashMap<String, Object>();
    // mapAccusationCondition.put("rec_id", accId);
    //
    // Map<String, Object> mapAccusation = contentAccusationMapper.getContentAccusation(mapAccusationCondition);
    // String accType = StrUtil.getMapValue(mapAccusation, "acc_type");
    // String accPerson = StrUtil.getMapValue(mapAccusation, "acc_person");
    //
    // Map<String, Object> noticeParam = new HashMap<String, Object>();
    // Map<String,Object> mapAccusationParam = new HashMap<String, Object>();
    // mapAccusationParam.put("acc_status", "done");
    // mapAccusationParam.put("handle_person", loginUser);
    // mapAccusationParam.put("handle_time", nowtime);
    // mapAccusationParam.put("handle_detail", param.get("check_reject_reason"));
    //
    //
    // count += this.updateNoRec(mapAccusationCondition, "d_accusation", mapAccusationParam);
    // if ("1".equals(check_status)) {
    //
    // if ("info".equals(accType)) {
    // String infoID = StrUtil.getMapValue(mapAccusation, "relation_id");
    // Map<String, Object> mapInfoCondition = new HashMap<String, Object>();
    // mapInfoCondition.put("rec_id", infoID);
    //
    // Map<String, Object> mapInfoParam = new HashMap<String, Object>();
    // mapInfoParam.put("info_status", "4");
    // this.update(mapInfoCondition, "d_info", mapInfoParam);
    //
    // Map<String, Object> infoDetail = infoContentMapper.getInfoContent(mapInfoCondition);
    // String infoTitle = StrUtil.getMapValue(infoDetail, "info_title");
    // String publishUnit = StrUtil.getMapValue(infoDetail, "publish_unit");
    // noticeParam.put("notice_title", "资讯被举报下架");
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("notice_content", "下架原因：" + param.get("acc_content") + "————" + infoTitle);
    // noticeParam.put("relation_id", param.get("rec_id"));
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_dept", publishUnit);
    //
    // count += this.saveNoRec("d_notice", noticeParam, false);
    //
    // Map<String, Object> accusationNoticePara = new HashMap<String, Object>();
    //
    // accusationNoticePara.put("notice_title", "举报成功");
    // accusationNoticePara.put("notice_type", "sys");
    // accusationNoticePara.put("notice_content", "您举报的文章：“ " + infoTitle + " ” ，经核实已下架，感谢您的监督");
    // accusationNoticePara.put("relation_id", param.get("rec_id"));
    // accusationNoticePara.put("notice_time", nowtime);
    // accusationNoticePara.put("notice_person", accPerson);
    // count += this.saveNoRec("d_notice", accusationNoticePara, false);
    //
    // // 推送消息
    // Map<String, Object> pushParam = new HashMap<String, Object>();
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", accPerson);
    // pushParam.put("push_value", "您举报的结果出来啦，快前往查看吧~");
    // pushParam.put("push_title", "举报处理结果");
    // personalPushService.send(pushParam);
    //
    // } else {
    // String commentID = StrUtil.getMapValue(mapAccusation, "relation_id");
    // Map<String, Object> mapCommentCondition = new HashMap<String, Object>();
    // mapCommentCondition.put("rec_id", commentID);
    //
    //
    // Map<String, Object> infoComment = infoCommentMapper.getInfoComment(mapCommentCondition);
    // String commentPerson = StrUtil.getMapValue(infoComment, "c_person");
    // String commentContent = StrUtil.getMapValue(infoComment, "c_content");
    // if (StrUtil.isNotNull(infoComment.get("info_id"))) {
    // Map<String, Object> mapInfoCondition = new HashMap<String, Object>();
    // mapInfoCondition.put("rec_id", StrUtil.getMapValue(param, "relation_id"));
    //
    // Map<String, Object> mapInfoParam = new HashMap<String, Object>();
    // mapInfoParam.put("comment_num", 1);
    //
    // this.updateDecrement(mapInfoCondition, "d_info", mapInfoParam);
    // } else {
    // Map<String, Object> mapActCondition = new HashMap<String, Object>();
    // mapActCondition.put("rec_id", StrUtil.getMapValue(param, "relation_id"));
    //
    // Map<String, Object> mapActParam = new HashMap<String, Object>();
    // mapActParam.put("comment_num", 1);
    //
    // this.updateDecrement(mapActCondition, "d_activity", mapActParam);
    // }
    // noticeParam.put("notice_title", "您的评论被举报，经核实，已删除");
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("notice_content", "原因：" + param.get("acc_content") + "————" + commentContent + " 请规范发言");
    // noticeParam.put("relation_id", param.get("rec_id"));
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_person", commentPerson);
    //
    // count += this.saveNoRec("d_notice", noticeParam, false);
    // // 推送消息
    // Map<String, Object> pushParam = new HashMap<String, Object>();
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", commentPerson);
    // pushParam.put("push_value", "您的评论被举报，经核实，已删除");
    // pushParam.put("push_title", "评论被举报");
    // personalPushService.send(pushParam);
    //
    // Map<String, Object> accusationNoticePara = new HashMap<String, Object>();
    //
    // accusationNoticePara.put("notice_title", "举报成功");
    // accusationNoticePara.put("notice_type", "sys");
    // accusationNoticePara.put("notice_content", "您举报的评论：“ " + commentContent + " ” ，经核实已删除，感谢您的监督");
    // accusationNoticePara.put("relation_id", param.get("rec_id"));
    // accusationNoticePara.put("notice_time", nowtime);
    // accusationNoticePara.put("notice_person", accPerson);
    // count += this.saveNoRec("d_notice", accusationNoticePara, false);
    //
    // this.deleteEmpty("d_info_comment", mapCommentCondition);
    //
    // // 推送消息
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", accPerson);
    // pushParam.put("push_value", "您举报的结果出来啦，快前往查看吧~");
    // pushParam.put("push_title", "举报处理结果");
    // personalPushService.send(pushParam);
    //
    // }
    // }else {
    //
    // if ("info".equals(accType)) {
    // String infoID = StrUtil.getMapValue(mapAccusation, "relation_id");
    // Map<String, Object> mapInfoCondition = new HashMap<String, Object>();
    // mapInfoCondition.put("rec_id", infoID);
    //
    // Map<String, Object> infoDetail = infoContentMapper.getInfoContent(mapInfoCondition);
    // String infoTitle = StrUtil.getMapValue(infoDetail, "info_title");
    // String publishUnit = StrUtil.getMapValue(infoDetail, "publish_unit");
    //
    // mapAccusationParam.put("acc_status", "reject");
    // count += this.updateNoRec(mapAccusationCondition, "d_accusation", mapAccusationParam);
    //
    // noticeParam.put("notice_title", "举报失败");
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("notice_content", "您对资讯：“ " + infoTitle + " ” 的举报不成立");
    // noticeParam.put("relation_id", param.get("rec_id"));
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_person", StrUtil.getMapValue(mapAccusation, "acc_person"));
    //
    // // 推送消息
    // Map<String, Object> pushParam = new HashMap<String, Object>();
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", accPerson);
    // pushParam.put("push_value", "您举报的结果出来啦，快前往查看吧~");
    // pushParam.put("push_title", "举报处理结果");
    // personalPushService.send(pushParam);
    //
    // count += this.saveNoRec("d_notice", noticeParam, false);
    // } else {
    // String commentID = StrUtil.getMapValue(mapAccusation, "relation_id");
    // Map<String, Object> mapCommentCondition = new HashMap<String, Object>();
    // mapCommentCondition.put("rec_id", commentID);
    //
    // Map<String, Object> infoComment = infoCommentMapper.getInfoComment(mapCommentCondition);
    // String commentPerson = StrUtil.getMapValue(infoComment, "c_person");
    // String commentContent = StrUtil.getMapValue(infoComment, "c_content");
    //
    // mapAccusationParam.put("acc_status", "reject");
    // count += this.updateNoRec(mapAccusationCondition, "d_accusation", mapAccusationParam);
    // noticeParam.put("notice_title", "举报失败");
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("notice_content", "您对用户：" + commentPerson + "的评论： “ " + commentContent + " ” 举报不成立");
    // noticeParam.put("relation_id", accId);
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_person", StrUtil.getMapValue(mapAccusation, "acc_person"));
    //
    // count += this.saveNoRec("d_notice", noticeParam, false);
    //
    // // 推送消息
    // Map<String, Object> pushParam = new HashMap<String, Object>();
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", accPerson);
    // pushParam.put("push_value", "您举报的结果出来啦，快前往查看吧~");
    // pushParam.put("push_title", "举报处理结果");
    // personalPushService.send(pushParam);
    //
    // }
    //
    // }
    //
    //
    // if (count > 0) {
    // return true;
    // } else {
    // return false;
    // }
    //
    // }

    /** 活动发布审核 **/
    private boolean checkActContent(Map<String, Object> param) {
        // 获取当前时间字符串
        String nowtime = DateUtils.getTime();
        // 获取参数里的关联ID作为活动的recID
        String actId = StrUtil.getMapValue(param, "relation_id");
        // 获取审核类型
        String checkStatus = StrUtil.getMapValue(param, "check_status");
        // 存储活动要修改的参数的map
        Map<String, Object> mapActivityParam = new HashMap<String, Object>();
        mapActivityParam.put("check_time", nowtime);
        // 存储活动修改的条件
        Map<String, Object> mapActCondition = new HashMap<String, Object>();
        mapActCondition.put("rec_id", actId);
        // 查询该活动的数据，便于后续使用
        Map<String, Object> mapActivity = activityMapper.getActivity(mapActCondition);

        // 创建存消息数据的map
        Map<String, Object> noticeParam = new HashMap<String, Object>();
        noticeParam.put("notice_type", "sys");
        noticeParam.put("relation_id", param.get("rec_id"));
        noticeParam.put("notice_time", nowtime);
        noticeParam.put("notice_dept", mapActivity.get("dept_id"));
        if (checkStatus.equals("1")) {
            mapActivityParam.put("act_status", "0");
            // 增加消息通知
            noticeParam.put("notice_title", "发布活动已通过");
            noticeParam.put("notice_content", "活动：“" + mapActivity.get("act_name") + "” 发布审核已通过");
        } else {
            mapActivityParam.put("act_status", "4");
            // 增加消息通知标题与内容
            noticeParam.put("notice_title", "发布活动未通过审核");
            noticeParam.put("notice_content", "活动：“" + mapActivity.get("act_name") + "” 发布审核被拒绝");
        }
        this.update(mapActCondition, "d_activity", mapActivityParam);
        this.saveNoRec("d_notice", noticeParam, false);

        return true;

    }

    /** 部门申请停用审核 **/
    // private boolean checkdeptStopContent(Map<String, Object> param) {
    // // TODO Auto-generated method stub
    // // 获取当前时间字符串
    // String nowtime = DateUtils.getTime();
    // // 通过关联的单位ID查出该单位数据，便于后续使用
    // Map<String, Object> mapDeptCondition = new HashMap<String, Object>();
    // mapDeptCondition.put("dept_id", param.get("relation_id"));
    // Map<String, Object> deptAlone = deptMapper.getDept(mapDeptCondition);
    //
    // // 取出审核结果，便于后续判断
    // Map<String, Object> mapDept = new HashMap<String, Object>();
    // String check_status = StrUtil.getMapValue(param, "check_status");
    // // 创建存消息数据的map
    // Map<String, Object> noticeParam = new HashMap<String, Object>();
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("relation_id", param.get("rec_id"));
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_dept", param.get("relation_id"));
    // // 判断审核结果，1-通过，2-不通过
    // if ("1".equals(check_status)) {
    // // 修改单位状态
    // mapDept.put("status", "1");
    // this.updateNoRec(mapDeptCondition, "sys_dept", mapDept);
    // // 更新单位用户状态
    // Map<String, Object> userUpdateCondition = new HashMap<String, Object>();
    // userUpdateCondition.put("dept_id", param.get("relation_id"));
    // userUpdateCondition.put("user_type", "dept");
    // Map<String, Object> userUpdateParam = new HashMap<String, Object>();
    // userUpdateParam.put("status", "6");
    // this.updateNoRec(userUpdateCondition, "sys_user", userUpdateParam);
    //
    // // 增加消息通知
    // noticeParam.put("notice_title", "申请已通过");
    // noticeParam.put("notice_content", deptAlone.get("dept_name") + "单位停用申请已通过");
    // } else {
    // noticeParam.put("notice_title", "申请未通过");
    // noticeParam.put("notice_content", deptAlone.get("dept_name") + "单位停用申请未通过");
    // }
    // this.saveNoRec("d_notice", noticeParam, false);
    // return true;
    // }

    /** 资讯审核 **/
    private boolean checkInfoContent(Map<String, Object> param) {
        // 获取当前时间字符串
        String nowtime = DateUtils.getTime();

        String loginUser = SecurityUtils.getUsername();
        // 创建新map存资讯状态
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        mapInfo.put("info_status", param.get("check_status"));
        mapInfo.put("check_time", nowtime);
        mapInfo.put("check_person", loginUser);
        mapInfo.put("check_reject_reason", param.get("check_reject_reason"));
        // 取出审核结果，便于后续判断
        String checkStatus = StrUtil.getMapValue(param, "check_status");
        // 资讯id储存使用
        Map<String, Object> mapInfoCondition = new HashMap<String, Object>();
        mapInfoCondition.put("rec_id", param.get("relation_id"));
        this.update(mapInfoCondition, "d_info", mapInfo);
        // 查出该审核的资讯的单条数据，便于后续使用
        Map<String, Object> infoAlone = infoContentMapper.getInfoContent(mapInfoCondition);

        // 创建存消息数据的map
        Map<String, Object> noticeParam = new HashMap<String, Object>();
        noticeParam.put("notice_type", "sys");
        noticeParam.put("relation_id", param.get("rec_id"));
        noticeParam.put("notice_time", nowtime);
        noticeParam.put("notice_dept", infoAlone.get("publish_unit"));
        // 判断审核结果，1-通过，2-不通过
        if (checkStatus.equals("1")) {
            noticeParam.put("notice_title", "资讯发布审核已通过");
            noticeParam.put("notice_content", infoAlone.get("info_title") + " 资讯发布审核已通过");
        } else {
            noticeParam.put("notice_title", "资讯发布审核未通过");
            noticeParam.put("notice_content", infoAlone.get("info_title") + " 资讯发布审核未通过");
        }

        this.saveNoRec("d_notice", noticeParam, false);

        return true;
    }

    /** 用户审核 **/
    // private boolean checkUserContent(Map<String, Object> param) {
    // String loginUser = SecurityUtils.getUsername();
    // String nowtime = DateUtils.getTime();
    // Map<String, Object> mapUser = new HashMap<String, Object>();
    // mapUser.put("update_by", loginUser);
    // mapUser.put("update_time", nowtime);
    // String userStatus = StrUtil.getMapValue(param, "check_status");
    // Map<String, Object> mapUserCondition = new HashMap<String, Object>();
    // mapUserCondition.put("user_id", param.get("relation_id"));
    // // 查询出该条用户的数据 ，便于后续使用
    // Map<String, Object> userAlone = userMapper.getSysUser(mapUserCondition);
    // if ("1".equals(userStatus)) {
    // mapUser.put("status", "1");
    // // 发送短信
    // String phone = StrUtil.getMapValue(userAlone, "phonenumber");
    // String templateCode = "SMS_219738981";
    // String signName = "即效编程";
    // Map<String, String> contentParam = new HashMap<String, String>();
    // // 发送短信验证码
    // Map<String, String> map;
    // try {
    // map = aliSendSmsService.sendSms(templateCode, phone, signName, contentParam);
    // } catch (ClientException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // } else if ("2".equals(userStatus)) {
    // mapUser.put("status", "4");
    // }
    // // 修改该用户的状态
    // this.updateNoRec(mapUserCondition, "sys_user", mapUser);
    //
    // return true;
    //
    // }

    // /** 评论审核 **/
    // private boolean checkCommentContent(Map<String, Object> param) {
    // // 获取当前时间字符串
    // String userName = SecurityUtils.getUsername();
    // String nowtime = DateUtils.getTime();
    // Map<String, Object> mapComment = new HashMap<String, Object>();
    // mapComment.put("c_status", param.get("check_status"));
    // // 取出审核结果，便于后续判断
    // String check_status = StrUtil.getMapValue(param, "check_status");
    // // 存储使用评论id
    // Map<String, Object> mapCommentCondition = new HashMap<String, Object>();
    // mapCommentCondition.put("rec_id", param.get("relation_id"));
    // this.updateNoRec(mapCommentCondition, "d_info_comment", mapComment);
    // // 查询出该审核的评论数据，便于后续使用
    // Map<String, Object> commentAlone = infoCommentMapper.getInfoComment(mapCommentCondition);
    //
    // // 推送消息
    // Map<String, Object> pushParam = new HashMap<String, Object>();
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", commentAlone.get("c_person"));
    //
    // // 创建存消息数据的map
    // Map<String, Object> noticeParam = new HashMap<String, Object>();
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("relation_id", param.get("rec_id"));
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_person", commentAlone.get("c_person"));
    // // 判断审核结果，1-通过，2-不通过
    // if ("1".equals(check_status)) {
    // noticeParam.put("notice_title", "评论被精选啦");
    // noticeParam.put("notice_content", "您的评论：“" + commentAlone.get("c_content") + "”被设为精选评论");
    //
    // // 发送kafka添加参数给消息机制处理
    // Map<String, Object> kafkaPointParam = new HashMap<String, Object>();
    // kafkaPointParam.put("affair_type", "comment");
    // kafkaPointParam.put("affair_time", nowtime);
    // kafkaPointParam.put("info_id", commentAlone.get("info_id"));
    // kafkaPointParam.put("affair_person", commentAlone.get("c_person"));
    // kafkaPointParam.put("affair_status", "0");
    // kafkaProducer.send(KafkaConst.TOPIC_POINTS, kafkaPointParam);
    //
    // pushParam.put("push_value", "您的评论被设为精选评论，快前往查看吧~");
    // pushParam.put("push_title", "评论被精选啦");
    //
    // } else {
    //
    // if (commentAlone.get("info_id") != null) {
    // Map<String, Object> mapCondition = new HashMap<String, Object>();
    // mapCondition.put("rec_id", commentAlone.get("info_id"));
    // Map<String, Object> mapcountParam = new HashMap<String, Object>();
    // mapcountParam.put("comment_num", 1);
    // this.updateDecrement(mapCondition, "d_info", mapcountParam);
    // } else {
    // Map<String, Object> mapCondition = new HashMap<String, Object>();
    // mapCondition.put("rec_id", commentAlone.get("activity_id"));
    // Map<String, Object> mapcountParam = new HashMap<String, Object>();
    // mapcountParam.put("comment_num", 1);
    // this.updateDecrement(mapCondition, "d_activity", mapcountParam);
    // }
    // }
    // return true;
    // }

    /** 民兵之家邀请审核 **/
    // private boolean checkfamilyContent(Map<String, Object> param) {
    // // 获取当前时间字符串
    // String nowtime = DateUtils.getTime();
    //
    // Map<String, Object> mapFamily = new HashMap<String, Object>();
    // Map<String, Object> mapFamilyCondition = new HashMap<String, Object>();
    // mapFamilyCondition.put("rec_id", param.get("relation_id"));
    // // 取出审核结果，便于后续判断
    // String check_status = StrUtil.getMapValue(param, "check_status");
    //
    // Map<String, Object> familyAlone = userFamilyMapper.getUserFamily(mapFamilyCondition);
    //
    //
    // // 推送消息
    // Map<String, Object> pushParam = new HashMap<String, Object>();
    // pushParam.put("push_type", "notice");
    // pushParam.put("push_person", familyAlone.get("user_name"));
    //
    // // 新建存消息数据的map
    // Map<String, Object> noticeParam = new HashMap<String, Object>();
    // noticeParam.put("notice_type", "sys");
    // noticeParam.put("relation_id", param.get("rec_id"));
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_person", familyAlone.get("user_name"));
    // if ("1".equals(check_status)) {
    // mapFamily.put("status", "1");
    // noticeParam.put("notice_title", "民兵之家邀请已通过");
    // noticeParam.put("notice_content", " 您当前单位管理员已通过" + familyAlone.get("dept_name") + "民兵之家的邀请");
    // pushParam.put("push_value", "民兵之家邀请已通过，快前往查看吧~");
    // pushParam.put("push_title", "民兵之家邀请已通过");
    // } else {
    // mapFamily.put("status", "3");
    // noticeParam.put("notice_title", "民兵之家邀请未通过");
    // noticeParam.put("notice_content", " 您当前单位管理员未通过" + familyAlone.get("dept_name") + "民兵之家的邀请");
    // pushParam.put("push_value", "民兵之家邀请未通过，快前往查看吧~");
    // pushParam.put("push_title", "民兵之家邀请未通过");
    // }
    // this.update(mapFamilyCondition, "d_user_family", mapFamily);
    //
    // this.saveNoRec("d_notice", noticeParam, false);
    // personalPushService.send(pushParam);
    //
    // return true;
    // }

    /** 部门操作审核，启动、增加 **/
    // private boolean checkdeptContent(Map<String, Object> param) {
    // Map<String, Object> mapDept = new HashMap<String, Object>();
    // String check_status = StrUtil.getMapValue(param, "check_status");
    // if ("1".equals(check_status)) {
    // mapDept.put("status", "0");
    // } else {
    // mapDept.put("status", "3");
    // }
    // Map<String, Object> mapDeptCondition = new HashMap<String, Object>();
    // mapDeptCondition.put("dept_id", param.get("relation_id"));
    // this.updateNoRec(mapDeptCondition, "sys_dept", mapDept);
    //
    // // 获取单条dept
    // Map<String, Object> mapDeptAlone = deptMapper.getDept(mapDeptCondition);
    //
    // Map<String, Object> mapUserCondition = new HashMap<String, Object>();
    // mapUserCondition.put("dept_id", param.get("relation_id"));
    // mapUserCondition.put("user_type", "dept");
    // Map<String, Object> mapDeptUser = userMapper.getDeptUser(mapUserCondition);
    // if (mapDeptUser != null) {
    // Map<String, Object> mapUserParam = new HashMap<String, Object>();
    // mapUserParam.put("status", '1');
    // Map<String, Object> mapUserAloneCondition = new HashMap<String, Object>();
    // mapUserAloneCondition.put("user_id", mapDeptUser.get("user_id"));
    // this.updateNoRec(mapUserAloneCondition, "sys_user", mapUserParam);
    // // 绑定用户角色
    // Map<String, Object> mapUserRoleParam = new HashMap<String, Object>();
    // mapUserRoleParam.put("user_id", mapDeptUser.get("user_id"));
    // mapUserRoleParam.put("role_id", "8");
    // Map<String, Object> mapUserRole = userMapper.getUserRole(mapUserRoleParam);
    // if (ObjUtil.isNull(mapUserRole)) {
    // this.saveNoRec("sys_user_role", mapUserRoleParam, false);
    // }
    // } else {
    // return true;
    // }
    //
    // return true;
    // }

	/** delCheckInfo : 删除审核信息表 **/
	public ResponseBodyVO delCheckInfo(Map<String, Object> param) {

		// TODO : 删除前的逻辑判断 

		// Table:d_check_info
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		mapCondition.put("rec_id", param.get("rec_id"));
		int count = this.delete("d_check_info",  mapCondition);

		return ServerRspUtil.formRspBodyVO(count, "删除审核信息表失败");
	}


}
