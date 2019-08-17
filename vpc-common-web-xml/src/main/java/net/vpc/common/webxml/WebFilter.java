package net.vpc.common.webxml;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebFilter {
    private String icon;
    private String filterName;
    private String displayName;
    private String description;
    private String filterClass;
    private Map<String,String> initParams=new LinkedHashMap<>();

    public String getIcon() {
        return icon;
    }

    public WebFilter setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getFilterName() {
        return filterName;
    }

    public WebFilter setFilterName(String filterName) {
        this.filterName = filterName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WebFilter setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getFilterClass() {
        return filterClass;
    }

    public WebFilter setFilterClass(String filterClass) {
        this.filterClass = filterClass;
        return this;
    }

    public Map<String, String> getInitParams() {
        return initParams;
    }

    public WebFilter setInitParams(Map<String, String> initParams) {
        this.initParams = initParams;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WebFilter setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}
