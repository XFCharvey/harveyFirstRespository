package com.cbox.base.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.cbox.base.constant.HttpStatus;
import com.cbox.base.core.domain.AjaxResult;
import com.cbox.base.core.page.PageDomain;
import com.cbox.base.core.page.TableDataInfo;
import com.cbox.base.core.page.TableSupport;
import com.cbox.base.utils.DateUtils;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.StringUtils;
import com.cbox.base.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * web层通用数据处理
 * 
 * @author cbox
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected final static String VALID_SUCC = "0";

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setData(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /** 把request参数转换成map */
    public Map<String, Object> copyParam(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String param = parameterNames.nextElement();
            params.put(param, request.getParameter(param));
        }
        return params;
    }

    public String validInput(String keys, Map param) {

        boolean bValid = true;
        String errMsg = "";
        if (StrUtil.isNotNull(keys)) {
            String[] keyArray = keys.split(",");
            for (String key : keyArray) {
                if (StrUtil.isNull(param.get(key))) {
                    errMsg += key + ",";
                    bValid = false;
                }
            }
        }

        if (!bValid) {
            errMsg = "必填参数[" + errMsg.substring(0, errMsg.length() - 1) + "]丢失";
        } else {
            errMsg = VALID_SUCC;
        }

        return errMsg;
    }
}
