package com.cbox.business.user.sysUser.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.business.user.sysUser.service.UserService;

@RestController
@RequestMapping("/business/user/sysUser")
public class UserController extends BaseController {
    @Autowired
    UserService userService;

    /** listUser : 查询用户数据 **/
    @RequestMapping(value = "listUser", method = RequestMethod.POST)
    public TableDataInfo listUser(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = userService.listUser(param);

        return getDataTable(list);
    }
    
    /** getCusWorkCheckCount : 获取我的客戶數量(浏览过的)，工作日志數量，待审核数量 **/
    @RequestMapping(value = "getCusWorkCheckCount", method = RequestMethod.POST)
    public ResponseBodyVO getCusWorkCheckCount(@RequestBody Map<String, Object> param) {
        return userService.getCusWorkCheckCount(param);
    }

    /** getCourseScore : 获取指定id的用户数据 **/
    @RequestMapping(value = "getSysUser", method = RequestMethod.POST)
    public ResponseBodyVO getSysUser(@RequestBody Map<String, Object> param) {

        return userService.getSysUser(param);
    }

    /** getCourseScore : 获取指定手机号的用户数据 **/
    @RequestMapping(value = "getSysUserByPhone", method = RequestMethod.POST)
    public ResponseBodyVO getSysUserByPhone(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("phonenumber", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return userService.getSysUser(param);
    }

    /** addUserdetail : 新增用户信息数据 **/
    @RequestMapping(value = "addUserdetail", method = RequestMethod.POST)
    public ResponseBodyVO addUserdetail(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_id,real_name,identity_type,id_number", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return userService.addUserdetail(param);
    }

    /** importBatchUser : 批量导入 **/
    @RequestMapping(value = "importBatchUser", method = RequestMethod.POST)
    public ResponseBodyVO importBatchUser(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_id,import_file,import_time", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return userService.importBatchUser(param);
    }


    /** updateUser : 修改用户数据 **/
    @RequestMapping(value = "updateUsers", method = RequestMethod.POST)
    public ResponseBodyVO updateUsers(@RequestBody Map<String, Object> param) {

        return userService.updateUsers(param);
    }

    /** updateUser : 修改用户状态 **/
    @RequestMapping(value = "updateUserStatus", method = RequestMethod.POST)
    public ResponseBodyVO updateUserStatus(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("user_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
        return userService.updateUserStatus(param);
    }


}
