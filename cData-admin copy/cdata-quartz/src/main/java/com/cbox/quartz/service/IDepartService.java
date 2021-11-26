package com.cbox.quartz.service;

import java.util.List;

import com.cbox.base.core.domain.entity.SysDept;

public interface IDepartService {

    /**
     */
    public List<SysDept> selectDeptList(SysDept dept);

    public void generateTask();

}
