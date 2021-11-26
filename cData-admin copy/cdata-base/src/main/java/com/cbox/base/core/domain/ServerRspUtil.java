package com.cbox.base.core.domain;

import java.util.List;
import java.util.Map;

import com.cbox.base.constant.HttpStatus;

/**
 * @ClassName: ServerRspUtil
 * @Function: ResponseBodyVO的操作类。
 * 
 * @author cbox
 * @date 2019年10月4日 上午10:08:12
 * @version 1.0
 */
public class ServerRspUtil {

    /***************** 指定信息生成ResponseBodyVO **************/
    // 成功
    public static ResponseBodyVO success(String msg, Object data) {
        ResponseBodyVO serverRsp = new ResponseBodyVO();
        serverRsp.setCode(HttpStatus.SUCCESS);
        serverRsp.setMsg(msg);
        serverRsp.setData(data);
        serverRsp.setSuccess(true);

        if (data instanceof List) {
            List listData = (List) data;
            if (listData != null && listData.size() > 0) {
                serverRsp.setTotal(listData.size());
            }
        }else if(data instanceof Map) {
            serverRsp.setTotal(1);
        }

        return serverRsp;
    }
    
    public static ResponseBodyVO success() {
        return success(null);
    }

    public static ResponseBodyVO success(Object data) {
        return success("操作成功", data);
    }

    // 失败
    public static ResponseBodyVO error(int code, String msg) {
        ResponseBodyVO serverRsp = new ResponseBodyVO();
        serverRsp.setCode(code);
        serverRsp.setMsg(msg);
        serverRsp.setSuccess(false);
        return serverRsp;
    }

    public static ResponseBodyVO error(String msg) {
        return error(HttpStatus.ERROR, msg); // 默认为业务失败
    }

    public static ResponseBodyVO error(Integer iCode, String msg) {
        return error(iCode, msg);
    }

    /***************** 根据传入状态iStatus自动生成ResponseBodyVO **************/
    public static ResponseBodyVO formRspBodyVO(int iStatus, Object data, String errMsg) {
        if (iStatus > 0) {
            return success(data);
        } else {
            return error(errMsg);
        }
    }

    public static ResponseBodyVO formRspBodyVO(int iStatus, Object data) {
        return formRspBodyVO(iStatus, data, "操作失败");
    }

    public static ResponseBodyVO formRspBodyVO(int iStatus, String errMsg) {
        return formRspBodyVO(iStatus, null, errMsg);
    }

}
