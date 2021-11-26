package com.cbox.wxlogin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.common.util.BaseTreeUtil;
import com.cbox.wxlogin.mapper.DeptSelectMapper;

@Service
@Transactional
public class DeptSelectService extends BaseService {

    @Autowired
    DeptSelectMapper deptSelectMapper;

    public ResponseBodyVO listDepttree(Map<String, Object> param) {
        // TODO Auto-generated method stub
        // 正常获取数据
        List<Map<String, Object>> list = deptSelectMapper.listDept(param);

        // 转换成前端所需的树形格式
        String id = "dept_id";
        String pId = "parent_id";
        String value = "dept_id";
        String name = "dept_name";
        boolean checked = false;
        List<Map<String, Object>> listReturn = BaseTreeUtil.toNodesForVueTree(list, id, pId, value, name, checked);

        return ServerRspUtil.formRspBodyVO(HttpStatus.SUCCESS, listReturn);
    }

}
