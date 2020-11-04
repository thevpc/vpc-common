package net.thevpc.common.webxml;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebServlet {
    private String icon;
    private String servletName;
    private String displayName;
    private String description;
    private String servletClass;
    private String jspFile;
    private Map<String,String> initParams=new LinkedHashMap<>();
    private int loadOnStartup;

    public String getIcon() {
        return icon;
    }

    public WebServlet setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getServletName() {
        return servletName;
    }

    public WebServlet setServletName(String servletName) {
        this.servletName = servletName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WebServlet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getServletClass() {
        return servletClass;
    }

    public WebServlet setServletClass(String servletClass) {
        this.servletClass = servletClass;
        return this;
    }

    public String getJspFile() {
        return jspFile;
    }

    public WebServlet setJspFile(String jspFile) {
        this.jspFile = jspFile;
        return this;
    }

    public Map<String, String> getInitParams() {
        return initParams;
    }

    public WebServlet setInitParams(Map<String, String> initParams) {
        this.initParams = initParams;
        return this;
    }

    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    public WebServlet setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WebServlet setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}
