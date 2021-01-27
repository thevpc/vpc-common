package net.thevpc.common.webxml;

import java.util.Objects;

public class WebFilterMapping {
    private String filterName;
    private String urlPattern;

    public WebFilterMapping() {
    }

    public WebFilterMapping(String filterName, String urlPattern) {
        this.filterName = filterName;
        this.urlPattern = urlPattern;
    }

    public String getFilterName() {
        return filterName;
    }

    public WebFilterMapping setFilterName(String filterName) {
        this.filterName = filterName;
        return this;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public WebFilterMapping setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebFilterMapping that = (WebFilterMapping) o;
        return Objects.equals(filterName, that.filterName) &&
                Objects.equals(urlPattern, that.urlPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterName, urlPattern);
    }
}
