package com.cbox.quartz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbox.base.utils.StringUtils;
import com.cbox.quartz.service.IDepartService;

/**
 * 部门任务
 * 
 * @author shenl
 *
 */
@Component("departTask")
public class DepartTask {

    @Autowired
    private IDepartService departService;

    public void generate() {
        System.out.println(StringUtils.format("开始生成部门task"));
        departService.generateTask();

    }
}
