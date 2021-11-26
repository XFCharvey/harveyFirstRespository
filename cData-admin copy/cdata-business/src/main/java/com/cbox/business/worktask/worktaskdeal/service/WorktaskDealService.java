package com.cbox.business.worktask.worktaskdeal.service;

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
import com.cbox.base.utils.StrUtil;
import com.cbox.business.worktask.worktaskdeal.mapper.WorktaskDealMapper;

/**
 * @ClassName: WorktaskDealService
 * @Function: 任务处理表
 * 
 * @author cbox
 * @version 1.0
 */
@Service
@Transactional
public class WorktaskDealService extends BaseService {

    @Autowired
    private WorktaskDealMapper worktaskDealMapper;

    /** listWorktaskDeal : 获取任务处理表列表数据 **/
    public List<Map<String, Object>> listWorktaskDeal(Map<String, Object> param) {
        super.appendUserInfo(param);

        List<Map<String, Object>> list = worktaskDealMapper.listWorktaskDeal(param);

        return list;
    }

    /** getWorktaskDeal : 获取指定id的任务处理表数据 **/
    public ResponseBodyVO getWorktaskDeal(Map<String, Object> param) {
        super.appendUserInfo(param);

        Map<String, Object> mapResult = worktaskDealMapper.getWorktaskDeal(param);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, mapResult);
    }

    /** addWorktaskDeal : 新增任务处理表 **/
    public ResponseBodyVO addWorktaskDeal(Map<String, Object> param) {
        int count = 0;
        // Table:d_worktask_deal
        String taskStatus = StrUtil.getMapValue(param, "task_status");
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("task_id", param.get("task_id"));
        mapParam.put("deal_detail", param.get("deal_detail"));
        mapParam.put("deal_image", param.get("deal_image"));
        mapParam.put("finish_rate", param.get("finish_rate"));
        count = this.save("d_worktask_deal", mapParam);
        // 对应任务的更新条件
        Map<String, Object> mapTaskCondition = new HashMap<String, Object>();
        mapTaskCondition.put("rec_id", StrUtil.getMapValue(param, "task_id"));
        // 任务的状态
        Map<String, Object> mapTaskParam = new HashMap<String, Object>();
        mapTaskParam.put("task_status", taskStatus);
        mapTaskParam.put("finish_rate", param.get("finish_rate"));
        count += this.update(mapTaskCondition, "d_worktask", mapTaskParam);

        return ServerRspUtil.formRspBodyVO(count, "新增任务表失败");
    }

    /** updateWorktaskDeal : 修改任务处理表 **/
    public ResponseBodyVO updateWorktaskDeal(Map<String, Object> param) {

        // Table:d_worktask_deal
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("task_id", param.get("task_id"));
        mapParam.put("deal_detail", param.get("deal_detail"));
        mapParam.put("deal_image", param.get("deal_image"));
        mapParam.put("finish_rate", param.get("finish_rate"));
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.update(mapCondition, "d_worktask_deal", mapParam);

        return ServerRspUtil.formRspBodyVO(count, "修改任务表失败");
    }

    /** delWorktaskDeal : 删除任务处理表 **/
    public ResponseBodyVO delWorktaskDeal(Map<String, Object> param) {

        // TODO : 删除前的逻辑判断

        // Table:d_worktask_deal
        Map<String, Object> mapCondition = new HashMap<String, Object>();
        mapCondition.put("rec_id", param.get("rec_id"));
        int count = this.delete("d_worktask_deal", mapCondition);

        return ServerRspUtil.formRspBodyVO(count, "删除任务表失败");
    }

}