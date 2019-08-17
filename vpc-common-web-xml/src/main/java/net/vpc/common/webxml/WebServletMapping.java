package net.vpc.common.webxml;

import java.util.Objects;

public class WebServletMapping {
    private String servletName;
    private String urlPattern;

    public WebServletMapping() {
    }

    public WebServletMapping(String servletName, String urlPattern) {
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    public String getServletName() {
        return servletName;
    }

    public WebServletMapping setServletName(String servletName) {
        this.servletName = servletName;
        return this;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public WebServletMapping setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebServletMapping that = (WebServletMapping) o;
        return Objects.equals(servletName, that.servletName) &&
                Objects.equals(urlPattern, that.urlPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(servletName, urlPattern);
    }
}
