package com.cbox.wxlogin;

import com.cbox.base.exception.BaseException;

/**
 * 用户信息异常类
 * 
 * @author cbox
 */
public class NoWeixinUserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public NoWeixinUserException(String message) {
        super("user", null, null, message);
    }
}
