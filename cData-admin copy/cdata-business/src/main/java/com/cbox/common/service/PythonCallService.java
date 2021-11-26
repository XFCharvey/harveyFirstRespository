package com.cbox.common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbox.base.config.PythonConfig;
import com.cbox.base.utils.ExceptionUtil;
import com.cbox.base.utils.file.FileUtils;

@Service
public class PythonCallService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PythonConfig pythonConfig;

    public boolean callPython(String[] cmdParams, String outFile) {

        // 强制创建目录
        try {
            FileUtils.forceMkdir(new File(outFile).getParentFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return callPython(cmdParams);
    }

    /**
     * callPython:
     *
     * @date: 2021年5月13日 下午10:11:15
     * @author qiu
     * @param cmdParams 第1个参数是python程序(xxx.py，不用管python文件的路径)，第2个开始是参数。
     */
    public boolean callPython(String[] cmdParams) {

        // 自动拼接命令参数
        int iLen = cmdParams.length;
        String[] pyArgs = new String[iLen + 1];
        for (int i = 0; i < pyArgs.length; i++) {
            if (i == 0) {
                pyArgs[i] = pythonConfig.getCompilerPython();
            } else if (i == 1) {
                pyArgs[i] = pythonConfig.getPyPath() + cmdParams[i - 1];
            } else {
                pyArgs[i] = cmdParams[i - 1];
            }
        }

        // 打印执行命令
        String cmdStr = "";
        for (int i = 0; i < pyArgs.length; i++) {
            cmdStr += pyArgs[i] + " ";
        }
        logger.info("Begin call python : {} ", cmdStr);

        // 调用python程序
        Process proc;
        BufferedReader br = null;
        try {
            proc = Runtime.getRuntime().exec(pyArgs);
            // 获取进程的标准输入流
            final InputStream is1 = proc.getInputStream();
            // 获取进城的错误流
            final InputStream is2 = proc.getErrorStream();
            br = new BufferedReader(new InputStreamReader(is1));

            // 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            new Thread() {
                public void run() {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
                    try {
                        String line1 = null;
                        while ((line1 = br1.readLine()) != null) {
                            System.out.println(line1);
                        }
                    } catch (IOException e) {
                        logger.error(ExceptionUtil.getExceptionMessage(e));
                    } finally {
                        try {
                            is1.close();
                            br1.close();
                        } catch (IOException e) {
                            logger.error(ExceptionUtil.getExceptionMessage(e));
                        }
                    }
                }
            }.start();

            new Thread() {
                public void run() {
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
                    try {
                        String line2 = null;
                        while ((line2 = br2.readLine()) != null) {
                            System.out.println(line2);
                        }
                    } catch (IOException e) {
                        logger.error(ExceptionUtil.getExceptionMessage(e));
                    } finally {
                        try {
                            is2.close();
                            br2.close();
                        } catch (IOException e) {
                            logger.error(ExceptionUtil.getExceptionMessage(e));
                        }
                    }
                }
            }.start();

            // 可能导致进程阻塞，甚至死锁
            int ret = proc.waitFor();
            logger.info("call python return value:{};exitCode:{}", ret, proc.exitValue());
        } catch (IOException e) {
            logger.error(ExceptionUtil.getExceptionMessage(e));
            return false;
        } catch (InterruptedException e) {
            logger.error(ExceptionUtil.getExceptionMessage(e));
            return false;
        } finally {
            FileUtils.closeIO(br);
        }

        return true;
    }

}
