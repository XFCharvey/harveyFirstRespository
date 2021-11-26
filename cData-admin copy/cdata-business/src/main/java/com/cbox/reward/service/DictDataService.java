package com.cbox.reward.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.base.core.domain.ServerRspUtil;
import com.cbox.base.core.service.BaseService;
import com.cbox.base.utils.StrUtil;

@Service
@Transactional
public class DictDataService extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 获取字典数据 */
    public ResponseBodyVO getDictData(@RequestBody Map<String, Object> param) {

        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("dict_type", StrUtil.getMapValue(param, "dict_type"));
        mapParam.put("orders", "dict_sort asc");
        List<Map<String, Object>> listDict = this.queryNoRec("sys_dict_data", mapParam);

        return ServerRspUtil.success(listDict);
    }

    /** 获取客户数据 */
    public ResponseBodyVO getCustomer(@RequestBody Map<String, Object> param) {

        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("rec_id", StrUtil.getMapValue(param, "cust_id"));

        Map<String, Object> mapCustomer = this.queryOneNoRec("d_customer", mapParam);

        return ServerRspUtil.success(mapCustomer);
    }

}
