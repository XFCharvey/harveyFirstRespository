package com.cbox.business.project.project.controller;

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
import com.cbox.business.project.project.service.ProjectService;

/**
 * @ClassName: ProjectController
 * @Function: 项目表
 * 
 * @author cbox
 * @version 1.0
 */
@RestController
@RequestMapping("/business/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    /** listProject : 获取项目表列表数据 **/
    @RequestMapping(value = "listProject", method = RequestMethod.POST)
    public TableDataInfo listProject(@RequestBody Map<String, Object> param) {

        startPage();
        List<Map<String, Object>> list = projectService.listProject(param);

        return getDataTable(list);
    }

    /** getProject : 获取指定id的项目表数据 **/
    @RequestMapping(value = "getProject", method = RequestMethod.POST)
    public ResponseBodyVO getProject(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return projectService.getProject(param);
    }

    /** addProject : 新增项目表 **/
    @RequestMapping(value = "addProject", method = RequestMethod.POST)
    public ResponseBodyVO addProject(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("project_name,project_type,project_person", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return projectService.addProject(param);
    }

    /** updateProject : 修改项目表 **/
    @RequestMapping(value = "updateProject", method = RequestMethod.POST)
    public ResponseBodyVO updateProject(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return projectService.updateProject(param);
    }

    /** delProject : 删除项目表 **/
    @RequestMapping(value = "delProject", method = RequestMethod.POST)
    public ResponseBodyVO delProject(@RequestBody Map<String, Object> param) {

        // 校验必填参数
        String checkResult = this.validInput("rec_id", param);
        if (!VALID_SUCC.equals(checkResult)) {
            return ServerRspUtil.error(checkResult);
        }

        return projectService.delProject(param);
    }

}