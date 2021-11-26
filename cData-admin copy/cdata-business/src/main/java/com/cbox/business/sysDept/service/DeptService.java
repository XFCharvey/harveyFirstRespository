package com.cbox.business.sysDept.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.SecurityUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.business.sysDept.mapper.DeptMapper;
import com.cbox.business.user.sysUser.mapper.UserMapper;
import com.cbox.common.util.BaseTreeUtil;

@Service
public class DeptService extends BaseService {

    @Autowired
    DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    public ResponseBodyVO listDepttree(Map<String, Object> param) {
        // TODO Auto-generated method stub
        super.appendUserInfo(param);
        // 正常获取数据
        List<Map<String, Object>> list = deptMapper.listDept(param);

        // 转换成前端所需的树形格式
        String id = "dept_id";
        String pId = "parent_id";
        String value = "dept_id";
        String name = "dept_name";
        boolean checked = false;
        List<Map<String, Object>> listReturn = BaseTreeUtil.toNodesForVueTree(list, id, pId, value, name, checked);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, listReturn);
    }

    public List<Map<String, Object>> listDept(Map<String, Object> param) {
        // TODO Auto-generated method stub
        super.appendUserInfo(param);

        List<Map<String, Object>> list = deptMapper.listDept(param);

        return list;
    }

    public ResponseBodyVO getDept(Map<String, Object> param) {
        super.appendUserInfo(param);

        Map<String, Object> mapResult = deptMapper.getDept(param);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
    }

    public ResponseBodyVO addDept(Map<String, Object> param) {
        // TODO Auto-generated method stub
        int count = 0;
        String deptId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId().toString();
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("parent_id", param.get("parent_id"));
        mapParam.put("ancestors", param.get("ancestors"));
        mapParam.put("dept_name", param.get("dept_name"));
        mapParam.put("dept_busi", param.get("dept_busi"));
        mapParam.put("dept_logo", param.get("dept_logo"));
        mapParam.put("dept_type", param.get("dept_type"));
        mapParam.put("dept_level", param.get("dept_level"));
        mapParam.put("order_num", param.get("order_num"));
        mapParam.put("leader", param.get("leader"));
        mapParam.put("phone", param.get("phone"));
        mapParam.put("email", param.get("email"));
        count = this.saveDept("sys_dept", mapParam);

        return ServerRspUtil.formRspBodyVO(count, "新增部门单位失败");
    }

    public ResponseBodyVO addSecondLevelDept(Map<String, Object> param) {
        // TODO Auto-generated method stub
        String nowtime = DateUtils.getTime();
        int count = 0;
        String deptId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId().toString();
        String anc = SecurityUtils.getLoginUser().getUser().getDept().getAncestors();
        String checkDept = SecurityUtils.getLoginUser().getUser().getDept().getParentId().toString();
        String parentId = "";
        if ("0".equals(checkDept)) {
            parentId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId().toString();
        } else {
            parentId = checkDept;
        }
        String userName = SecurityUtils.getUsername();
        String deptName = SecurityUtils.getLoginUser().getUser().getDept().getDeptName();
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("parent_id", deptId);
        mapParam.put("ancestors", anc + "," + deptId);
        mapParam.put("dept_name", param.get("dept_name"));
        mapParam.put("dept_busi", param.get("dept_busi"));
        mapParam.put("dept_logo", param.get("dept_logo"));
        mapParam.put("dept_level", param.get("dept_level"));
        mapParam.put("dept_type", param.get("dept_type"));
        mapParam.put("order_num", param.get("order_num"));
        mapParam.put("create_by", userName);
        mapParam.put("create_time", nowtime);
        mapParam.put("update_by", userName);
        mapParam.put("update_time", nowtime);
        count = this.saveDept("sys_dept", mapParam);

        String deptID = StrUtil.getMapValue(mapParam, "dept_id");
        Map<String, Object> mapUserParam = new HashMap<String, Object>();
        mapUserParam.put("dept_id", deptID);
        mapUserParam.put("user_name", param.get("user_name"));

        Date date = new Date();
        // 将手机号设置未当前时间的格林威治时间数值
        long datetime = date.getTime();
        String phoneNum = String.valueOf(datetime).substring(2);
        String passWord;
        if (StrUtil.getMapValue(param, "password") != null) {
            passWord = StrUtil.getMapValue(param, "password");
        } else {
            passWord = "818181";
        }
        String encPassword = SecurityUtils.encryptPassword(passWord);
        mapUserParam.put("password", encPassword);
        mapUserParam.put("user_type", "dept");
        mapUserParam.put("avatar", param.get("dept_logo"));
        mapUserParam.put("nick_name", param.get("dept_name"));
        mapUserParam.put("real_name", param.get("dept_name"));
        mapUserParam.put("status", 2);
        mapUserParam.put("phonenumber", phoneNum);
        count = this.saveNoRec("sys_user", mapUserParam, false);

        Map<String, Object> mapCheckParam = new HashMap<String, Object>();
        mapCheckParam.put("dept_id", parentId);
        mapCheckParam.put("check_type", "dept");
        mapCheckParam.put("relation_id", deptID);
        mapCheckParam.put("check_title", "新增部门申请");
        mapCheckParam.put("publish_unitid", deptId);
        mapCheckParam.put("publish_time", nowtime);
        mapCheckParam.put("check_desc", deptName + "单位申请增加" + param.get("dept_name") + "二级单位");
        count = this.save("d_check_info", mapCheckParam);

        String newCheckID = StrUtil.getMapValue(mapCheckParam, "rec_id");

        Map<String, Object> noticeParam = new HashMap<String, Object>();
        noticeParam.put("notice_title", "单位新增等待审核");
        noticeParam.put("notice_type", "check");
        noticeParam.put("relation_id", newCheckID);
        noticeParam.put("notice_content", deptName + "单位申请增加" + param.get("dept_name") + "二级单位");
        noticeParam.put("notice_time", nowtime);
        noticeParam.put("notice_dept", parentId);
        count += this.saveNoRec("d_notice", noticeParam, false);

        return ServerRspUtil.formRspBodyVO(count, "新增部门单位失败");
    }

    public ResponseBodyVO updateDept(Map<String, Object> param) {
        // TODO Auto-generated method stub
        // Table:sys_dept
        int count = 0;
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("parent_id", param.get("parent_id"));
        mapParam.put("ancestors", param.get("ancestors"));
        mapParam.put("dept_name", param.get("dept_name"));
        mapParam.put("dept_type", param.get("dept_type"));
        mapParam.put("dept_level", param.get("dept_level"));
        mapParam.put("dept_busi", param.get("dept_busi"));
        mapParam.put("dept_type", param.get("dept_type"));
        mapParam.put("order_num", param.get("order_num"));
        mapParam.put("dept_logo", param.get("dept_logo"));
        mapParam.put("leader", param.get("leader"));
        mapParam.put("phone", param.get("phone"));
        mapParam.put("email", param.get("email"));
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("dept_id", param.get("dept_id"));

        Map<String, Object> deptCondition = new HashMap<String, Object>();
        deptCondition.put("check_type", "dept");
        deptCondition.put("relation_id", param.get("dept_id"));
        deptCondition.put("check_status", "0");
        count += this.updateNoRec(mapCondition, "sys_dept", mapParam);

        return ServerRspUtil.formRspBodyVO(count, "修改部门信息失败");
    }

    public ResponseBodyVO resetDeptPassword(Map<String, Object> param) {
        // TODO Auto-generated method stub
        // Table:sys_dept
        int count = 0;
        String password = "818181";
        String encPassword = SecurityUtils.encryptPassword(password);
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("password", encPassword);
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("dept_id", param.get("dept_id"));
        mapCondition.put("user_type", "dept");

        count = this.updateNoRec(mapCondition, "sys_user", mapParam);
        return ServerRspUtil.formRspBodyVO(count, "重置部门密码失败");
    }



    // public ResponseBodyVO updateDeptStatus(Map<String, Object> param) {
    // // TODO Auto-generated method stub
    // // Table:sys_dept
    // int count = 0;
    // String dept_status = StrUtil.getMapValue(param, "status");
    // switch (dept_status) {
    // case "0":
    // boolean res = startDept(param);
    // if (res) {
    // count += 1;
    // } else {
    // return ServerRspUtil.formRspBodyVO(count, "已提交申请，请耐心等待审核结果");
    // }
    // break;
    // case "1":
    // boolean result = stopDept(param);
    // if (result) {
    // count += 1;
    // } else {
    // return ServerRspUtil.formRspBodyVO(count, "已提交申请，请耐心等待审核结果");
    // }
    // break;
    // default:
    // return ServerRspUtil.formRspBodyVO(count, "暂不支持此状态类型");
    // }
    //
    // return ServerRspUtil.formRspBodyVO(count, "修改部门信息失败");
    // }

    // private boolean startDept(Map<String, Object> param) {
    // // 获取当前登录的用户的单位的上级单位
    // String checkDept = SecurityUtils.getLoginUser().getUser().getDept().getParentId().toString();
    // String parentId = "";
    // // 如果没有上级单位，就取当前自身单位作为审核单位
    // if ("0".equals(checkDept)) {
    // parentId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId().toString();
    // } else {
    // parentId = checkDept;
    // }
    // // 获取当前时间字符串
    // String nowtime = DateUtils.getTime();
    // Map<String, Object> mapParam = new HashMap<String, Object>();
    // mapParam.put("dept_id", parentId);
    // mapParam.put("check_type", "dept");
    // mapParam.put("relation_id", param.get("dept_id"));
    // mapParam.put("check_title", "部门启用申请");
    // mapParam.put("publish_time", nowtime);
    // mapParam.put("check_desc", param.get("dept_name") + "单位申请启用，等待审核");
    // // 审核数据的参数
    // Map<String, Object> deptCondition = new HashMap<String, Object>();
    // deptCondition.put("check_type", "dept");
    // deptCondition.put("relation_id", param.get("dept_id"));
    // deptCondition.put("check_status", "0");
    // List<Map<String, Object>> listDeptCheck = checkInfoMapper.listCheckInfo(deptCondition);
    // // 判断当前单位是否有待审核的审核数据
    // if (listDeptCheck != null && listDeptCheck.size() > 0) {
    // return false;
    // } else {
    // this.save("d_check_info", mapParam);
    // String newCheckID = StrUtil.getMapValue(mapParam, "rec_id");
    // Map<String, Object> noticeParam = new HashMap<String, Object>();
    // noticeParam.put("notice_title", "单位申请启用等待审核");
    // noticeParam.put("notice_type", "check");
    // noticeParam.put("relation_id", newCheckID);
    // noticeParam.put("notice_content", param.get("dept_name") + "单位申请启用，等待审核");
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_dept", parentId);
    // this.saveNoRec("d_notice", noticeParam, false);
    // return true;
    // }
    //
    // }

    // private boolean stopDept(Map<String, Object> param) {
    // // 获取当前登录的用户的单位的上级单位
    // String checkDept = SecurityUtils.getLoginUser().getUser().getDept().getParentId().toString();
    // String parentId = "";
    // // 如果没有上级单位，就取当前自身单位作为审核单位
    // if ("0".equals(checkDept)) {
    // parentId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId().toString();
    // } else {
    // parentId = checkDept;
    // }
    // // 获取当前时间字符串
    // String nowtime = DateUtils.getTime();
    // Map<String, Object> mapParam = new HashMap<String, Object>();
    // mapParam.put("dept_id", parentId);
    // mapParam.put("check_type", "deptstop");
    // mapParam.put("relation_id", param.get("dept_id"));
    // mapParam.put("check_title", "部门停用申请");
    // mapParam.put("publish_time", nowtime);
    // mapParam.put("check_desc", param.get("dept_name") + "申请停用单位，等待审核");
    // Map<String, Object> deptCondition = new HashMap<String, Object>();
    // deptCondition.put("check_type", "deptstop");
    // deptCondition.put("relation_id", param.get("dept_id"));
    // deptCondition.put("check_status", "0");
    // List<Map<String, Object>> listDeptCheck = checkInfoMapper.listCheckInfo(deptCondition);
    // if (listDeptCheck != null && listDeptCheck.size() > 0) {
    // return false;
    // } else {
    // this.save("d_check_info", mapParam);
    // String newCheckID = StrUtil.getMapValue(mapParam, "rec_id");
    // Map<String, Object> noticeParam = new HashMap<String, Object>();
    // noticeParam.put("notice_title", "单位申请停用等待审核");
    // noticeParam.put("notice_type", "check");
    // noticeParam.put("relation_id", newCheckID);
    // noticeParam.put("notice_content", param.get("dept_name") + "单位申请停用，等待审核");
    // noticeParam.put("notice_time", nowtime);
    // noticeParam.put("notice_dept", parentId);
    // this.saveNoRec("d_notice", noticeParam, false);
    // return true;
    // }
    // }

    /** 删除部门 **/
    public ResponseBodyVO delDept(Map<String, Object> param) {
        // TODO Auto-generated method stub
        int count = 0;
        String status = StrUtil.getMapValue(param, "status");
        Map<String, Object> deptPam = new HashMap<String, Object>();
        deptPam.put("del_flag", "2");
        Map<String, Object> deptCondition = new HashMap<String, Object>();
        deptCondition.put("dept_id", StrUtil.getMapValue(param, "dept_id"));
        if ("1".equals(status) && "0".equals(status)) {
            return ServerRspUtil.formRspBodyVO(count, "该部门无法删除，请重新选择");
        } else if ("2".equals(status)) {
            count += updateNoRec(deptCondition, "sys_dept", deptPam);

            Map<String, Object> checkCondition = new HashMap<String, Object>();
            checkCondition.put("relation_id", StrUtil.getMapValue(param, "dept_id"));
            checkCondition.put("check_type", "dept");
            checkCondition.put("check_status", "0");
            count += this.delete("d_check_info", checkCondition);

            Map<String, Object> deptUserCondition = new HashMap<String, Object>();
            deptUserCondition.put("dept_id", StrUtil.getMapValue(param, "dept_id"));
            deptUserCondition.put("user_type", "dept");
            count += userMapper.deleteDeptUser(deptUserCondition);

            return ServerRspUtil.formRspBodyVO(count, "删除部门失败");

        } else {
            count += updateNoRec(deptCondition, "sys_dept", deptPam);
            return ServerRspUtil.formRspBodyVO(count, "删除部门失败");
        }
    }

}
