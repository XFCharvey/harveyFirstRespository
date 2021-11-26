package com.cbox.business.worktask.worktaskdeal.controller;

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
import com.cbox.business.worktask.worktaskdeal.service.WorktaskDealService;

/**
 * @ClassName: WorktaskDealController
 * @Function: 任务处理表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/worktask/worktaskdeal")
public class WorktaskDealController extends BaseController {

    @Autowired
    private WorktaskDealService worktaskDealService;

    /** listWorktaskDeal : 获取任务处理表列表数据 **/
    @RequestMapping(value = "listWorktaskDeal", method = RequestMethod.POST)
    public TableDataInfo listWorktaskDeal(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = worktaskDealService.listWorktaskDeal(param);

        return getDataTable(list);
    }

    /** getWorktaskDeal : 获取指定id的任务处理表数据 **/
    @RequestMapping(value = "getWorktaskDeal", method = RequestMethod.POST)
    public ResponseBodyVO getWorktaskDeal(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return worktaskDealService.getWorktaskDeal(param);
    }

    /** addWorktaskDeal : 新增任务处理表 **/
    @RequestMapping(value = "addWorktaskDeal", method = RequestMethod.POST)
    public ResponseBodyVO addWorktaskDeal(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("task_id,deal_detail", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return worktaskDealService.addWorktaskDeal(param);
    }

    /** updateWorktaskDeal : 修改任务处理表 **/
    @RequestMapping(value = "updateWorktaskDeal", method = RequestMethod.POST)
    public ResponseBodyVO updateWorktaskDeal(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return worktaskDealService.updateWorktaskDeal(param);
    }

    /** delWorktaskDeal : 删除任务处理表 **/
    @RequestMapping(value = "delWorktaskDeal", method = RequestMethod.POST)
    public ResponseBodyVO delWorktaskDeal(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return worktaskDealService.delWorktaskDeal(param);
    }

}