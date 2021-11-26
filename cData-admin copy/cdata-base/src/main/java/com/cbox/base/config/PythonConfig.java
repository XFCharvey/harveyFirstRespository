package com.cbox.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 */
@Component
@ConfigurationProperties("python.config")
public class PythonConfig {
    private String pyPath;
    private String compilerPython;

    public String getPyPath() {
        return pyPath;
    }

    public void setPyPath(String pyPath) {
        this.pyPath = pyPath;
    }

    public String getCompilerPython() {
        return compilerPython;
    }

    public void setCompilerPython(String compilerPython) {
        this.compilerPython = compilerPython;
    }

}
