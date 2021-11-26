package com.cbox.business.sysDept.controller;

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
import com.cbox.business.sysDept.service.DeptService;

@RestController
@RequestMapping("/business/sysDept")
public class DeptController extends BaseController {
    @Autowired
    DeptService deptService;

    /** listUser : 查询部门单位数据 **/
    @RequestMapping(value = "listDept", method = RequestMethod.POST)
    public TableDataInfo listDept(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = deptService.listDept(param);

        return getDataTable(list);
    }

    /** listInfoMenutree : 获取部门列表返回树形数据 **/
    @RequestMapping(value = "listDepttree", method = RequestMethod.POST)
    public ResponseBodyVO listDepttree(@RequestBody Map<String, Object> param) {

        return deptService.listDepttree(param);
    }

    @RequestMapping(value = "getDept", method = RequestMethod.POST)
    public ResponseBodyVO getDept(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return deptService.getDept(param);
    }

    /** addInfoMenu : 新增部门 **/
    @RequestMapping(value = "addDept", method = RequestMethod.POST)
    public ResponseBodyVO addDept(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("parent_id,ancestors,dept_name", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return deptService.addDept(param);
    }

    /** addInfoMenu : 新增二级部门 **/
    @RequestMapping(value = "addSecondLevelDept", method = RequestMethod.POST)
    public ResponseBodyVO addSecondLevelDept(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_name", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return deptService.addSecondLevelDept(param);
    }

    /** updateInfoMenu : 修改部门 **/
    @RequestMapping(value = "updateDept", method = RequestMethod.POST)
    public ResponseBodyVO updateDept(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return deptService.updateDept(param);
    }

    /** updateInfoMenu : 重置部门账号的密码 **/
    @RequestMapping(value = "resetDeptPassword", method = RequestMethod.POST)
    public ResponseBodyVO resetDeptPassword(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return deptService.resetDeptPassword(param);
    }
    //
    // /** updateInfoMenu : 修改部门状态 **/
    // @RequestMapping(value = "updateDeptStatus", method = RequestMethod.POST)
    // public ResponseBodyVO updateDeptStatus(@RequestBody Map<String, Object> param) {
    //
    // // 校验必填参数
    // String checkResult = this.validInput("dept_id,dept_name,status", param);
    // if (!VALID_SUCC.equals(checkResult)) {
    // return ServerRspUtil.error(checkResult);
    // }
    //
    // return deptService.updateDeptStatus(param);
    // }

    /** updateInfoMenu : 删除待审核或者审核不通过的部门 **/
    @RequestMapping(value = "delDept", method = RequestMethod.POST)
    public ResponseBodyVO delDept(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("dept_id,status", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return deptService.delDept(param);
    }

}
