package com.cbox.business.hdayinfomanage.controller;
 
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
 
import com.cbox.base.core.controller.BaseController;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.page.TableDataInfo;
 
 
import com.cbox.business.hdayinfomanage.service.HdayInfoManageService;
 
 
/**
 * @ClassName: HdayInfoManageController
 * @Function: 节日信息管理
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/hdayinfomanage")
public class HdayInfoManageController extends BaseController {
 
    @Autowired
    private HdayInfoManageService hdayInfoManageService;
 
    
    /** listHdayInfoManage : 获取节日信息管理列表数据 **/
    @RequestMapping(value = "listHdayInfoManage", method = RequestMethod.POST)
    public TableDataInfo listHdayInfoManage(@RequestBody Map<String, Object> param) {
 
        startPage();
        List<Map<String, Object>> list= hdayInfoManageService.listHdayInfoManage(param);
 
        return getDataTable(list);
    }
 
    /** getHdayInfoManage : 获取指定id的节日信息管理数据 **/
    @RequestMapping(value = "getHdayInfoManage", method = RequestMethod.POST)
    public ResponseBodyVO getHdayInfoManage(@RequestBody Map<String, Object> param) {
 
        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
 
        return hdayInfoManageService.getHdayInfoManage(param);
    }
 
    /** addHdayInfoManage : 新增节日信息管理 **/
    @RequestMapping(value = "addHdayInfoManage", method = RequestMethod.POST)
    public ResponseBodyVO addHdayInfoManage(@RequestBody Map<String, Object> param) {
 
        // 校验必填参数
        String checkResult = this.validInput("holiday_date,date_name,sex,content", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
 
        return hdayInfoManageService.addHdayInfoManage(param);
    }
 
    /** updateHdayInfoManage : 修改节日信息管理 **/
    @RequestMapping(value = "updateHdayInfoManage", method = RequestMethod.POST)
    public ResponseBodyVO updateHdayInfoManage(@RequestBody Map<String, Object> param) {
 
        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
 
        return hdayInfoManageService.updateHdayInfoManage(param);
    }
 
    /** delHdayInfoManage : 删除节日信息管理 **/
    @RequestMapping(value = "delHdayInfoManage", method = RequestMethod.POST)
    public ResponseBodyVO delHdayInfoManage(@RequestBody Map<String, Object> param) {
 
        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }
 
        return hdayInfoManageService.delHdayInfoManage(param);
    }
 
 
}