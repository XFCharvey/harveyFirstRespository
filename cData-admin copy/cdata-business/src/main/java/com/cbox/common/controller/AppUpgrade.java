package com.cbox.common.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbox.base.core.domain.ResponseBodyVO;
import com.cbox.common.service.AppVersionService;

@RestController
public class AppUpgrade {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AppVersionService service;

    /**
     * 1、app版本检查
     */
    @GetMapping("/update")
    public ResponseBodyVO update(String appid, String version, String osname, String force) {
        log.info("appid={};version={};osname={},force={}", appid, version, osname, force);
        return service.checkVersion(appid, version, osname, force);
    }

    /**
     * 1、注册app信息
     */
    @PostMapping("/regApp")
    public ResponseBodyVO regApp(@RequestBody Map<String, String> param) {
        System.out.println(param);
        return service.regApp(param);
    }

}
