package com.itdom.edge.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2.sys.parameter")
public class SystemParameter {

    private List<String> ignoreUrls;

    public SystemParameter() {
    }

    public SystemParameter(List<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public List<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(List<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }
}
