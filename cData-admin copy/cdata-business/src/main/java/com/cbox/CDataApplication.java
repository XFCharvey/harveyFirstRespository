package com.cbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author cbox
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class CDataApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(CDataApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ cdata 系统启动成功  ");
    }
}
