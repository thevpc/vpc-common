package net.thevpc.common.webxml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebXml {
    private List<WebServlet> servlets=new ArrayList<>();
    private List<WebFilter> filters =new ArrayList<>();
    private List<WebListener> listeners =new ArrayList<>();
    private List<WebServletMapping> servletMappings=new ArrayList<>();
    private List<WebFilterMapping> filterMappings=new ArrayList<>();
    private Map<String,String> contextParams=new LinkedHashMap<>();
    private WebSessionConfig sessionConfig;

    public WebSessionConfig getSessionConfig() {
        return sessionConfig;
    }

    public WebXml setSessionConfig(WebSessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
        return this;
    }

    public List<WebServlet> getServlets() {
        return servlets;
    }

    public List<WebServletMapping> getServletMappings() {
        return servletMappings;
    }

    public WebXml setServlets(List<WebServlet> servlets) {
        this.servlets = servlets;
        return this;
    }

    public WebXml setServletMappings(List<WebServletMapping> servletMappings) {
        this.servletMappings = servletMappings;
        return this;
    }

    public Map<String, String> getContextParams() {
        return contextParams;
    }

    public WebXml setContextParams(Map<String, String> contextParams) {
        this.contextParams = contextParams;
        return this;
    }

    public List<WebFilter> getFilters() {
        return filters;
    }

    public WebXml setFilters(List<WebFilter> filters) {
        this.filters = filters;
        return this;
    }

    public List<WebFilterMapping> getFilterMappings() {
        return filterMappings;
    }

    public WebXml setFilterMappings(List<WebFilterMapping> filterMappings) {
        this.filterMappings = filterMappings;
        return this;
    }

    public List<WebListener> getListeners() {
        return listeners;
    }

    public WebXml setListeners(List<WebListener> listeners) {
        this.listeners = listeners;
        return this;
    }
}
