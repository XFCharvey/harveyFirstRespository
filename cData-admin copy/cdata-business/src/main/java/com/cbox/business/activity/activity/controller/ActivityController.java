package com.cbox.business.activity.activity.controller;

import java.io.IOException;
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
import com.cbox.business.activity.activity.service.ActivityService;
import com.google.zxing.WriterException;

/**
 * @ClassName: ActivityController
 * @Function: 线下活动表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/activity")
public class ActivityController extends BaseController {

    @Autowired
    ActivityService activityService;

    /** listActivity : web获取活动列表数据 **/
    @RequestMapping(value = "listActivity", method = RequestMethod.POST)
    public TableDataInfo listActivity(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = activityService.listActivity(param, true);

        return getDataTable(list);
    }

    /** listActivityApp : app获取活动列表数据 **/
    @RequestMapping(value = "listActivityApp", method = RequestMethod.POST)
    public TableDataInfo listActivityApp(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = activityService.listActivity(param, false);

        return getDataTable(list);
    }

    /** getActivity : 获取指定id的数据 **/
    @RequestMapping(value = "getActivity", method = RequestMethod.POST)
    public ResponseBodyVO getActivity(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return activityService.getActivity(param);
    }

    /** addActivity : 新增表数据 
     * @throws WriterException 
     * @throws IOException **/
    @RequestMapping(value = "addActivity", method = RequestMethod.POST)
    public ResponseBodyVO addActivity(@RequestBody Map<String, Object> param) throws IOException, WriterException {

        // 校验必填参数
        String checkResult = this.validInput("act_name", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return activityService.addActivity(param);
    }

    /** updateActivity : 修改活动表数据 
     * @throws WriterException 
     * @throws IOException **/
    @RequestMapping(value = "updateActivity", method = RequestMethod.POST)
    public ResponseBodyVO updateActivity(@RequestBody Map<String, Object> param) throws IOException, WriterException {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return activityService.updateActivity(param);
    }

    /** delActivity : 删除活动 **/
    @RequestMapping(value = "delActivity", method = RequestMethod.POST)
    public ResponseBodyVO delActivity(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return activityService.delActivity(param);
    }

}
