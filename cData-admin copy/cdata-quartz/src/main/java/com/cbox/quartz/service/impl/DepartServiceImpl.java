package com.cbox.quartz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.core.domain.entity.SysDept;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.UniqId;
import com.cbox.quartz.mapper.DepartMapper;
import com.cbox.quartz.service.IDepartService;

@Service
public class DepartServiceImpl implements IDepartService {

    @Autowired
    private DepartMapper mapper;

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return mapper.selectDeptList(dept);
    }

    @Override
    public void generateTask() {
        List<SysDept> depts = this.selectDeptList(new SysDept());
        for (int i = 0; i < depts.size(); i++) {
            SysDept dept = depts.get(i);
            if (!hasChild(dept, depts)) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("task_name", dept.getDeptName() + "任务-" + DateUtils.dateTimeNow());
                map.put("depart_id", String.valueOf(dept.getDeptId())); // 部门id
                // map.put("user_id", dept.getLeader());// 负责人
                map.put("task_file", UniqId.getInstance().getUniqTime() + ".tiff");
                map.put("task_status", "0");
                mapper.insertTask(map);
            }
        }
    }

    /**
     * 当前部门是否有子部门
     * 
     * @param dept
     * @param depts
     * @return
     */
    private boolean hasChild(SysDept dept, List<SysDept> depts) {
        for (int i = 0; i < depts.size(); i++) {
            if (dept.getDeptId().equals(depts.get(i).getParentId())) {
                return true;
            }
        }
        return false;
    }

}
