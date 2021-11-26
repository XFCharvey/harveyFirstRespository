package com.cbox.base.exception.file;

import com.cbox.base.exception.BaseException;

/**
 * 文件信息异常类
 * 
 * @author cbox
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
