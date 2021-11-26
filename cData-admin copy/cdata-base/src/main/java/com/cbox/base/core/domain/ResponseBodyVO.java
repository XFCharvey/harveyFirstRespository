package com.cbox.base.core.domain;

import com.cbox.base.constant.HttpStatus;

/**
 * 
 * @ClassName: ResponseBodyVO
 * @Function: 服务端给客户端的返回数据Body格式，与AjaxResult作用一样。controller与service间也使用这个格式交互，省去数据转换给客户端。
 * 
 * @author cbox
 * @date 2020年2月27日 上午11:14:00
 * @version 1.0
 */
public class ResponseBodyVO {

    /** 对象属性 */
    int code;// 返回编码。成功的编码是200
    String msg;// 返回消息
    Object data;// 返回数据
    int total;// 返回数据的记录数（只有返回查询数据需要）
    private boolean success;//是否成功

    /** 扩展方法 */
    public boolean success() {
        boolean isSuccess = false;
        if (HttpStatus.SUCCESS == code) {
            isSuccess = true;
        }
        return isSuccess;
    }

    /** getter setter */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
